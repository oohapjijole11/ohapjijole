package com.sparta.final_project.domain.auction.service;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.AuctionRedis;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.bid.repository.EmitterRepository;
import com.sparta.final_project.domain.bid.service.BidCommonService;
import com.sparta.final_project.domain.bid.service.BidService;
import com.sparta.final_project.domain.bid.service.SbidService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionProgressService {

    private final AuctionRepository auctionRepository;
    private final SbidService sbidService;
    private final BidService bidService;
    private final BidCommonService commonService;
    private final EmitterRepository emitterRepository;
    private static final Long DEFAULT_TIMEOUT = 60L * 1000 * 30;
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(100);

    //    경매 남은시간
    public SseEmitter startAuctionCountdown(Long auctionId, String lastEventId) {
        Auction auction = auctionRepository.findByIdAndStatus(auctionId, Status.BID);
//        sseEmitter id 만들기
        String emitterId = commonService.makeTimeIncludeId(auctionId);
        SseEmitter sseEmitter = emitterRepository.save(emitterId, new SseEmitter(DEFAULT_TIMEOUT));
//       tory.deleteById(emitt 완료된 sseEmitterId 와 30분이 지난 sseEmitterId 삭제
        sseEmitter.onCompletion(() -> emitterRepository.deleteAllEmitterStartWithAuctionId(String.valueOf(auctionId)));
        sseEmitter.onTimeout(() -> emitterRepository.deleteById(emitterId));

        final ScheduledFuture<?>[] scheduledFuture = new ScheduledFuture<?>[auctionId.intValue()];

        scheduledFuture[auctionId.intValue() - 1] = executorService.scheduleAtFixedRate(() -> {
            try {
                Duration remainingTime = Duration.between(LocalDateTime.now(), auction.getEndTime());
                if (remainingTime.isNegative() || remainingTime.isZero()) {
                    sbidService.createSbid(auctionId);
                    sseEmitter.send("경매가 마감되었습니다");
                    scheduledFuture[auctionId.intValue() - 1].cancel(false);
                } else {
                    String formattedTime = formatRemainingTime(remainingTime);
                    sseEmitter.send(formattedTime);
                    if (!lastEventId.isEmpty() && hasLostData(lastEventId)) {
                        sendLostData(sseEmitter, lastEventId);
                    }
                }
            } catch (IOException e) {
                emitterRepository.deleteById(emitterId);
            }
        }, 0, 1, TimeUnit.SECONDS);

        return sseEmitter;
    }

    //    //    경매 시작
    @Scheduled(cron = "0 * * * * *")
    public void monitorAuctionStart() {
        List<Auction> auctions = auctionRepository.findAllByStatus(Status.WAITING);
        LocalDateTime now = LocalDateTime.now();
        boolean updated = false;
        for (Auction auction : auctions) {
            if (now.isEqual(auction.getStartTime()) || now.isAfter(auction.getStartTime())) {
                auction.setStatus(Status.BID);
                commonService.startNotification(auction.getId());
                updated = true;
            }
        }
        if (updated) {
            auctionRepository.saveAll(auctions);
        }
    }


    private boolean hasLostData(String lastEventId) {
        return !lastEventId.isEmpty();
    }

    private void sendLostData(SseEmitter sseEmitter, String lastEventId) {
//    누락된 이벤트 검색
        Map<String, Object> missedEvents = emitterRepository.findEventsSinceLastId(lastEventId);
        missedEvents.entrySet().stream()
                .sorted(Map.Entry.comparingByKey()) //
                .forEach(entry -> {
                    String eventId = entry.getKey();
                    Object eventData = entry.getValue();
                    try {
                        sseEmitter.send(SseEmitter.event()
                                .id(eventId)
                                .data(eventData));
                    } catch (IOException e) {
                        log.error("누락된 이벤트를 보내는도중 오류가 발생 (ID: {}) to client: {}", eventId, e.getMessage());
//                        오류발생시 루프 중단
                        return;
                    }
                });
//        오래된 이벤트 제거
        emitterRepository.cleanUpOldEvents(lastEventId);
    }

    //    시간 메소드
    public String formatRemainingTime(Duration remainingTime) {
        long days = remainingTime.toDays();
        long hours = remainingTime.toHoursPart();
        long minutes = remainingTime.toMinutesPart();
        long seconds = remainingTime.toSecondsPart();
        return String.format("%d 일, %d 시간, %d 분, %d 초 남은시간", days, hours, minutes, seconds);
    }
}
