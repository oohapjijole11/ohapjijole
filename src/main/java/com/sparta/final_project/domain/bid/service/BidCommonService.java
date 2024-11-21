package com.sparta.final_project.domain.bid.service;

import com.sparta.final_project.domain.aop.DistributedLock;
import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.bid.entity.Bid;
import com.sparta.final_project.domain.bid.repository.BidRepository;
import com.sparta.final_project.domain.bid.repository.EmitterRepository;
import com.sparta.final_project.domain.bid.repository.RedisRepository;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class BidCommonService {

    private final EmitterRepository emitterRepository;
    private final RedisRepository redisRepository;
    private final BidRepository bidRepository;

    private static final long RECONNECTION_TIMEOUT = 1000L;

    //emitter와 event의 id값 만들기(경매 번호 + 시간)
    public String makeTimeIncludeId(Long auctionId) {  // 데이터 유실 시점 파악 위함
        return auctionId + "_"+ System.currentTimeMillis();
    }

    // 특정 SseEmitter 를 이용해 알림을 보냅니다. SseEmitter 는 최초 연결 시 생성되며,
    // 해당 SseEmitter 를 생성한 클라이언트로 알림을 발송하게 됩니다.
    public void sendToClient(SseEmitter emitter, String name, String emitterId, String eventId, Object data) {
        try {
            emitter.send(SseEmitter.event()
                    .name(name)
                    .id(eventId)
                    .data(data)
                    .reconnectTime(RECONNECTION_TIMEOUT));
            log.info("알림 발생 : {}_{}", eventId, data);
        }catch(IllegalStateException e) {   //서버가 일부로 중지시켰을때 나타났음
            emitterRepository.deleteById(emitterId);
            throw new OhapjijoleException(ErrorCode._SSE_NOT_CONNECT);
        } catch (IOException exception) {
            emitterRepository.deleteById(emitterId);
            throw new OhapjijoleException(ErrorCode._SSE_NOT_CONNECT);
        }
    }

    @Async
    public void sseSend(Bid bid, Status status) {
        String eventId = makeTimeIncludeId(bid.getAuction().getId());
        // 해당 경매장의 모든 SseEmitter 가져옴
        Map<String, SseEmitter> emitters = emitterRepository
                .findAllEmitterStartWithByAuctionId(String.valueOf(bid.getAuction().getId()));
        // 데이터 캐시 저장 (유실된 데이터 처리 위함)
        redisRepository.setBid(eventId, bid.getPrice());

        //해당 경매장의 모든 사용자들에게 데이터 전송
        //입찰일때
        switch (status) {
            case BID:
                emitters.forEach(
                        (key, emitter) -> {
                            // 데이터 전송
                            sendToClient(emitter,"new price", key, eventId, bid.getPrice()+" 원");
                        }
                );
                break;
            //낙찰일때
            case SUCCESSBID:
                emitters.forEach(
                    (key, emitter) -> {
                        // 데이터 전송
                        sendToClient(emitter, "BID SUCCESS", key, eventId, bid.getPrice() + " 원");
                        //연결 끊기
                        emitter.complete();
                        //관리 콜렉션에서 지움
                        emitterRepository.deleteById(key);
                    }
                );
                break;
            //유찰일때
            case FAILBID:
                emitters.forEach(
                    (key, emitter) -> {
                        // 데이터 전송
                        sendToClient(emitter, "BID FAIL", key, eventId, "경매가 유찰되었습니다. 다음 경매를 기다려주세요.");
                        //연결 끊기
                        emitter.complete();
                        //관리 콜렉션에서 지움
                        emitterRepository.deleteById(key);
                    }
                );
                break;
            default:
                throw new OhapjijoleException(ErrorCode._INVALID_STATUS);
        }

    }

    @Async
    public void startNotification(Long auctionId) {
        String eventId = makeTimeIncludeId(auctionId);
        // 해당 경매장의 모든 SseEmitter 가져옴
        Map<String, SseEmitter> emitters = emitterRepository
                .findAllEmitterStartWithByAuctionId(String.valueOf(auctionId));
        //접속해있는 모든 참여자들에게 시작을 알림
        emitters.forEach(
                (key, emitter) -> {
                    // 데이터 전송
                    sendToClient(emitter,"bid start", key, eventId, "경매를 시작합니다!");
                }
        );
    }

    @DistributedLock(key = "auctionBid", dynamicKey = "#auctionId")
    protected Bid saveBid(Long auctionId, int price, Auction auction, User user) {
        //메서드를 대상으로 로그 파일을 적을 logger를 따로 지정
        Logger logger = LoggerFactory.getLogger("bid_logger");
        //redis에서 최신 입찰 데이터 가져오는 방식
        int lastprice = redisRepository.findLastBidPrice(String.valueOf(auctionId));
        int maxBid = lastprice==0 ? auction.getStartPrice()-1 : lastprice;
        if(price<=maxBid) {
            //로그 파일에 적지 않기 때문에 log 사용
            log.warn("입찰가 : {} 최고 입찰가 : {}", price, maxBid);
            throw new OhapjijoleException(ErrorCode._NOT_LARGER_PRICE," 현재 입찰가 : "+ price + " 최고 입찰가 : "+maxBid);
        }

        //입찰 데이터 생성 및 저장
        Bid bid = new Bid(price, user, auction);
        Bid newBid = bidRepository.save(bid);
        //로그 파일에 적을 로그일 경우 logger 사용
        logger.info("bid ::: userId : {} auctionId : {} price : {}", user.getId(), auctionId, price);

        //저장된 데이터 실시간 알림 보내기
        sseSend(newBid, Status.BID);
        return newBid;
    }
}
