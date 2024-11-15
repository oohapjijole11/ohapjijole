package com.sparta.final_project.domain.bid.service;

import com.slack.api.Slack;
import com.slack.api.model.Attachment;
import com.slack.api.model.Field;
import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.bid.dto.response.SbidResponse;
import com.sparta.final_project.domain.bid.dto.response.SbidSimpleResponse;
import com.sparta.final_project.domain.bid.entity.Bid;
import com.sparta.final_project.domain.bid.entity.Sbid;
import com.sparta.final_project.domain.bid.repository.BidRepository;
import com.sparta.final_project.domain.bid.repository.SbidRepository;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
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

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.slack.api.webhook.WebhookPayloads.payload;

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

    private final Slack slackClient = Slack.getInstance();

    //낙찰
    @Transactional
    public SbidResponse createSbid(Long auctionId) {
        Logger logger = LoggerFactory.getLogger("sbid_logger");
        //해당 경매 찾기
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(()-> new OhapjijoleException(ErrorCode._NOT_FOUND_AUCTION));
        //이미 끝난 경매인지 체크
        if(auction.getStatus()==Status.SUCCESSBID||auction.getStatus()==Status.FAILBID) throw new OhapjijoleException(ErrorCode._BID_STATUS_END);
        //아직 시작 안한 경매인지 체크
        if(auction.getStatus()==Status.WAITING) throw new OhapjijoleException(ErrorCode._BID_STATUS_BEFORE);
        //경매 상태를 낙찰로 변경
        auction.bidSuccess(Status.SUCCESSBID, LocalDateTime.now());
        //마지막 입찰 정보 가져오기
        Bid lastBid = bidRepository.findAllByAuctionOrderByCreatedAtDesc(auction).get(0);
        //낙찰자 정보 가져오기
        User sBidder = lastBid.getUser();
        //낙찰 데이터 생성 및 저장
        Sbid sbid = new Sbid(sBidder, auction,lastBid.getPrice());
        Sbid saveSbid = sbidRepository.save(sbid);
        logger.info("sbid ::: userId : {} auctionId : {} price : {}", sBidder.getId(), auctionId, lastBid.getPrice());
        //낙찰 알림 보내고 실시간 연결 끊기
        commonService.sseSend(lastBid, Status.SUCCESSBID);

        //낙찰자에게 slack 알림 보내기
        sendSlack(sBidder.getSlackUrl(), saveSbid);


        return new SbidResponse(saveSbid);
    }

    //낙찰 목록 조회
    public Page<SbidSimpleResponse> getSbids(Long userId, int page, int size) {
        userRepository.findById(userId).orElseThrow(()-> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Sbid> sbids = sbidRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
        return sbids.map(SbidSimpleResponse::new);
    }

    //낙찰때 슬랙 알림
    public void sendSlack(String slackUrl,Sbid sbid) {
        Auction auction = sbid.getAuction();
        String title = "낙찰 소식 알림이";
        String message = auction.getItem().getName()+" 상품을 낙찰하셨습니다. 지금 확인해보세요!";
        String fieldTitle = auction.getTitle()+" 경매 낙찰 안내";
        String fieldContent = "상품 이름 : "+auction.getItem().getName()+"\n 낙찰 일시 : "+sbid.getAuction().getEndTime().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH시 mm분 ss초"))+ "\n 낙찰 가격 : "+sbid.getPrice()+" 원";
        try{
            slackClient.send(slackUrl, payload(p -> p
                    .text(title) // 메시지 제목
                    .iconUrl("https://raw.githubusercontent.com/oohapjijole11/ohapjijole/refs/heads/dev/src/main/resources/static/brandimage.webp")
                    .username("ohapjijole")
                    .attachments(List.of(
                            Attachment.builder()
                                    .color("#ff0000") // 메시지 색상
                                    .pretext(message)// 메시지 본문 내용
                                    .fields(List.of(
                                            new Field(fieldTitle, fieldContent, false)
                                    ))
                                    .build())))
            );
        } catch (IOException e) {
            e.printStackTrace();
            throw new OhapjijoleException(ErrorCode._NOT_AVAILABLE_SLACK_NOTIFICATION);
        }

    }



}
