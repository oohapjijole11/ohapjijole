package com.sparta.final_project.domain.bid.service;

import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.bid.entity.Bid;
import com.sparta.final_project.domain.bid.repository.EmitterRepository;
import com.sparta.final_project.domain.bid.repository.RedisRepository;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            log.info(eventId + "_" + data);
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
        redisRepository.setbid(eventId, bid.getPrice());

        //해당 경매장의 모든 사용자들에게 데이터 전송
        //입찰일때
        if(status == Status.BID) {
            emitters.forEach(
                    (key, emitter) -> {
                        // 데이터 전송
                        sendToClient(emitter,"new price", key, eventId, bid.getPrice()+" 원");
                    }
            );
        }
        //낙찰일때
        else if(status == Status.SUCCESSBID) {
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
        }
        //유찰일때
        else if(status == Status.FAILBID) {
            emitters.forEach(
                    (key, emitter) -> {
                        // 데이터 전송
                        sendToClient(emitter, "BID FAIL", key, eventId, "다음 경매를 기다려주세요.");
                        //연결 끊기
                        emitter.complete();
                        //관리 콜렉션에서 지움
                        emitterRepository.deleteById(key);
                    }
            );
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
}
