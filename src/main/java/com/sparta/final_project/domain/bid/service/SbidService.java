package com.sparta.final_project.domain.bid.service;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.bid.dto.response.EndBidResponse;
import com.sparta.final_project.domain.bid.dto.response.FBidResponse;
import com.sparta.final_project.domain.bid.dto.response.SbidResponse;
import com.sparta.final_project.domain.bid.dto.response.SbidSimpleResponse;
import com.sparta.final_project.domain.bid.entity.Bid;
import com.sparta.final_project.domain.bid.entity.Sbid;
import com.sparta.final_project.domain.bid.repository.BidRepository;
import com.sparta.final_project.domain.bid.repository.SbidRepository;
import com.sparta.final_project.domain.common.entity.Color;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.common.service.SlackService;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class SbidService {
    private final SbidRepository sbidRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final BidCommonService commonService;
    private final SlackService slackService;

    //낙찰
    @Transactional
    public EndBidResponse createSbid(Long auctionId) {
        Logger logger = LoggerFactory.getLogger("sbid_logger");
        //해당 경매 찾기
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(()-> new OhapjijoleException(ErrorCode._NOT_FOUND_AUCTION));
        //이미 끝난 경매인지 체크
        if(auction.getStatus()==Status.SUCCESSBID||auction.getStatus()==Status.FAILBID) throw new OhapjijoleException(ErrorCode._BID_STATUS_END);
        //아직 시작 안한 경매인지 체크
        if(auction.getStatus()==Status.WAITING) throw new OhapjijoleException(ErrorCode._BID_STATUS_BEFORE);
        //경매 상태를 낙찰로 변경
//        auction.bidSuccess(Status.SUCCESSBID, LocalDateTime.now());
        //경매 입찰목록 가져오기
        List<Bid> bids = bidRepository.findAllByAuctionOrderByCreatedAtDesc(auction);
        //입찰이 있으면 낙찰, 없으면 유찰
        if(!bids.isEmpty()) {
            //마지막 입찰 정보 가져오기
            Bid lastBid = bidRepository.findAllByAuctionOrderByCreatedAtDesc(auction).get(0);
            //낙찰자 정보 가져오기
            User sBidder = lastBid.getUser();
            //경매 상태 낙찰로 변경
            auction.setStatus(Status.SUCCESSBID);
            //낙찰 데이터 생성 및 저장
            Sbid sbid = new Sbid(sBidder, auction,lastBid.getPrice());
            Sbid saveSbid = sbidRepository.save(sbid);
            logger.info("sbid ::: userId : {} auctionId : {} price : {}", sBidder.getId(), auctionId, lastBid.getPrice());
            //낙찰 알림 보내고 실시간 연결 끊기
            commonService.sseSend(lastBid, Status.SUCCESSBID);

            //낙찰자에게 slack 알림 보내기
            sendPurchaseSlack(sBidder.getSlackUrl(), saveSbid);
            
            //판매자에게 slack 알림 보내기
            sendSaleSlack(auction.getItem().getUser().getSlackUrl(), saveSbid);

            return new SbidResponse(saveSbid);
        }
        else {
            auction.setStatus(Status.FAILBID);
            commonService.sseSend(null, Status.FAILBID);
            return new FBidResponse(auction);

        }
    }

    //낙찰 목록 조회
    public Page<SbidSimpleResponse> getSbids(Long userId, int page, int size) {
        userRepository.findById(userId).orElseThrow(()-> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Sbid> sbids = sbidRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
        return sbids.map(SbidSimpleResponse::new);
    }

    //낙찰때 슬랙 알림
    private void sendPurchaseSlack(String slackUrl,Sbid sbid) {

        Auction auction = sbid.getAuction();
        String title = "낙찰 소식 알림이";
        String message = auction.getItem().getName()+" 상품을 낙찰하셨습니다. 지금 확인해보세요!";
        String fieldTitle = "["+auction.getTitle()+"]"+" 낙찰 안내";
        String fieldContent = "상품 이름 : "+auction.getItem().getName()+"\n 낙찰 일시 : "+sbid.getAuction().getEndTime().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"))+ "\n 낙찰 가격 : "+sbid.getPrice()+" 원";
        slackService.sendSlackMessage(slackUrl, title, Color.RED, message, fieldTitle, fieldContent);
    }
    
    //낙찰때 판매자에게 슬랙 알림
    private void sendSaleSlack(String slackUrl,Sbid sbid) {
        Auction auction = sbid.getAuction();
        String title = "판매 소식 알림이";
        String message = auction.getItem().getName()+" 상품이 판매되었습니다. 지금 확인해보세요!";
        String fieldTitle = "["+auction.getTitle()+"]"+" 낙찰 안내";
        String fieldContent = "\t 상품 이름 : "+auction.getItem().getName()+"\n 구매자 : "+sbid.getUser().getName()+"\n 낙찰 일시 : "+sbid.getAuction().getEndTime().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"))+ "\n 낙찰 가격 : "+sbid.getPrice()+" 원";
        slackService.sendSlackMessage(slackUrl, title, Color.YELLOW, message, fieldTitle, fieldContent);
    }
}
