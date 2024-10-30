package com.sparta.final_project.domain.auction.service;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.bid.service.SbidService;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.message.SimpleMessage;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionProgressService{

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final SbidService sbidService;
    private SseEmitter sseEmitter;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();

    //    경매 남은시간
    public SseEmitter startAuctionCountdown(Long auctionId) {
//        1시간 제한
        this.sseEmitter = new SseEmitter(60 * 60 * 1000L);
        Auction auction = auctionRepository.findByIdAndStatus(auctionId, Status.BID);
        if (auction == null) {
            throw new OhapjijoleException(ErrorCode._NOT_FOUND_AUCTION);
        }
        executor.submit(() -> {
            try {
                while (true) {
                    Duration remainingTime = Duration.between(LocalDateTime.now(), auction.getEndTime());

                    if (remainingTime.isNegative() || remainingTime.isZero()) {
                        sseEmitter.send("마감!");
//                        마감 시간이 되면 SbidCreate메소드 실행
                        sbidService.createSbid(auctionId);
                        sseEmitter.complete();
                        break;
                    } else {
                        long days = remainingTime.toDays();
                        long hours = remainingTime.toHoursPart();
                        long minutes = remainingTime.toMinutesPart();
                        long seconds = remainingTime.toSecondsPart();
                        String formattedTime = String.format(
                                "%d 일, %d 시간, %d 분, %d 초 남은시간",
                                days, hours, minutes, seconds
                        );
                        sseEmitter.send(formattedTime);
                    }

                    Thread.sleep(1000);
                }
            } catch (IOException | InterruptedException e) {
                sseEmitter.completeWithError(e);
            }
        });
        return sseEmitter;
    }

    //    경매 시작 리스트로 만들어서 경매 하나 뿐만 아니고 나머지 경매들도 자동으로 Status.BID로 변경
    public SseEmitter monitorAuctionStart() {
//        1시간 제한
        this.sseEmitter = new SseEmitter(60 * 60 * 1000L);
        executor.submit(() -> {
            try {
                while (true) {
                    List<Auction> auctions = auctionRepository.findAllByStatus(Status.WAITING);
                    LocalDateTime now = LocalDateTime.now();
//                    auction Status가 WATING인 것만 조회
                    for (Auction auction : auctions) {
                        if (now.isEqual(auction.getStartTime()) || now.isAfter(auction.getStartTime())) {
                            auction.setStatus(Status.BID);
                            auctionRepository.save(auction);
//                        경매 티켓을 가진 유저에게 알림보내기 추가예정
                            sseEmitter.send(auction.getItem().getName() + " 경매 시작!");
                        }
                    }
                    Thread.sleep(1000);
                }
            } catch (IOException | InterruptedException e) {
                sseEmitter.completeWithError(e);
            }
        });

        return sseEmitter;
    }
}
