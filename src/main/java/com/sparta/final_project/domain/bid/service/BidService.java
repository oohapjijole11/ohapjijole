package com.sparta.final_project.domain.bid.service;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.bid.dto.request.BidRequest;
import com.sparta.final_project.domain.bid.dto.response.BidResponse;
import com.sparta.final_project.domain.bid.dto.response.BidSimpleResponse;
import com.sparta.final_project.domain.bid.entity.Bid;
import com.sparta.final_project.domain.bid.repository.BidRepository;
import com.sparta.final_project.domain.bid.repository.EmitterRepository;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.ticket.entity.BuyTickets;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class BidService {

    private final EmitterRepository emitterRepository;
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final BidCommonService commonService;

    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 30;   //한번 들어가면 30분동안 sse로 연결됨


    //경매장 입장
    @Transactional
    public SseEmitter subscribe(AuthUser authUser, Long auctionId, String lastEventId) {
        //todo 경매가 입찰 없이 끝나면 유찰로 넘어가게 변경 필요
        User user = userRepository.findById(authUser.getId()).orElseThrow(()-> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        //옥션 있는지 확인
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(()-> new OhapjijoleException(ErrorCode._NOT_FOUND_AUCTION));

        //유저가 해당 경매장의 티켓 있는지 확인
        checkTicket(auctionId, user);

        //경매 시작전 20분 부터 입장 가능
        if(auction.getStatus()==Status.WAITING && ChronoUnit.MINUTES.between(auction.getStartTime(), LocalDateTime.now()) > 20) throw new OhapjijoleException(ErrorCode._BID_STATUS_BEFORE);
        //경매가 끝났다면 끝났다고 오류날림
        if(auction.getStatus()== Status.SUCCESSBID || auction.getStatus()== Status.FAILBID) throw new OhapjijoleException(ErrorCode._BID_STATUS_END);

        //sseemitter id 만들기
        String emitterId = commonService.makeTimeIncludeId(auctionId);
        //새 sseemitter만들어서 emitterRepository로 관리
        SseEmitter emitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));

        emitter.onCompletion(() -> emitterRepository.deleteById(emitterId));
        emitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        //연결 event의 id 만들기
        String eventId = commonService.makeTimeIncludeId(auctionId);
        //연결되었다고 알림 보내기
        commonService.sendToClient(emitter, "sse", emitterId, eventId,
                "연결되었습니다. EventStream Created. [auctionId=" + auctionId + "]");

        //경매 시작 전이면 몇시에 시작한다고 날림
        if(auction.getStatus()==Status.WAITING) {
            eventId = commonService.makeTimeIncludeId(auctionId);
            commonService.sendToClient(emitter, "not start", emitterId, eventId, auction.getStartTime()+" 에 경매가 시작됩니다");
        }
        //해당 경매장의 처음 시작 금액 알림
        eventId = commonService.makeTimeIncludeId(auctionId);
        commonService.sendToClient(emitter, "start price", emitterId, eventId,
                "최저가 : "+auction.getStartPrice() + " 원");

        //해당 경매의 지난 알림들 가져오기
        Map<String, Object> events = emitterRepository.findAllEventCacheStartWithAuctionId(
                String.valueOf(auctionId));
        //만약 중간에 연결이 끝났었다면 그 이후의 알림을 보냄
        if (!lastEventId.isEmpty()) {
            events.entrySet().stream()
                    .filter(entry -> lastEventId.compareTo(entry.getKey()) < 0)
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> commonService.sendToClient(emitter,"new price", emitterId, entry.getKey(),
                            entry.getValue()));
        //처음 들어왔다면 해당 경매장의 모든 알림을 보냄
        } else {
            events.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry ->
                            commonService.sendToClient(emitter,"new price", emitterId, entry.getKey(),
                                    entry.getValue()));
        }
        return emitter;
    }

    private static void checkTicket(Long auctionId, User user) {
        Optional<BuyTickets> ticket = user.getTickets().stream()
                .filter(t-> t.getTicket().getAuction().getId().equals(auctionId))
                .findFirst();
        if (ticket.isEmpty()) throw new OhapjijoleException(ErrorCode._NOT_HAVE_TICKET);
    }

    //입찰
    @Transactional
    public BidResponse createBid(Long userId, Long auctionId, BidRequest request) {
        //user와 경매장이 있는지 확인하기
        User user = userRepository.findById(userId).orElseThrow(()-> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_AUCTION));
        //유저가 해당 경매장의 티켓 있는지 확인
        checkTicket(auctionId, user);
        //경매장이 경매중인지 확인하기
        if(auction.getStatus()!= Status.BID) throw new OhapjijoleException(ErrorCode._BID_NOT_GOING);
        //입찰 금액 조건을 최저가 이상 또는 최고 입찰가 초과로 지정
        List<Bid> bidList = bidRepository.findAllByAuctionOrderByCreatedAtDesc(auction);
        int maxBid = bidList.isEmpty() ? auction.getStartPrice()-1 : bidList.get(0).getPrice();
        if(request.getPrice()<=maxBid) throw new OhapjijoleException(ErrorCode._NOT_LARGER_PRICE);
        
        //입찰 데이터 생성 및 저장
        Bid bid = new Bid(request, user, auction);
        Bid newBid = bidRepository.save(bid);
        
        //저장된 데이터 실시간 알림 보내기
        commonService.sseSend(newBid, Status.BID);
        return new BidResponse(newBid);
    }

    public List<BidSimpleResponse> getBids(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_AUCTION));
        List<Bid> bidList = bidRepository.findAllByAuctionOrderByCreatedAtDesc(auction);
        return bidList.stream().map(BidSimpleResponse::new).toList();
    }



}
