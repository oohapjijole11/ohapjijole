package com.sparta.final_project.domain.auction.service;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.bid.service.BidCommonService;
import com.sparta.final_project.domain.bid.service.SbidService;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionProgressService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final SbidService sbidService;
    private SseEmitter sseEmitter;
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final BidCommonService commonService;

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
                        auction.setStatus(Status.SUCCESSBID);
                        auctionRepository.save(auction);
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

//    경매 시작
    public SseEmitter monitorAuctionStart(AuthUser authUser, Long auctionId) {
//        1시간 제한
//        this.sseEmitter = new SseEmitter(60 * 60 * 1000L);
        log.info("auction start : auctionId : "+auctionId);
        userRepository.findById(authUser.getId()).orElseThrow(() -> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_AUCTION));

        executor.submit(() -> {
            try {
                while (true) {
                    LocalDateTime now = LocalDateTime.now();
                    if (now.isAfter(auction.getStartTime()) || now.isEqual(auction.getStartTime())) {
                        auction.setStatus(Status.BID);
                        auctionRepository.save(auction);
                        //접속해 있는 모든 사용자들에게 시작되었음을 알림
                        commonService.startNotification(auctionId);
//                        sseEmitter.send("경매 시작!");  //접속한 사람들에게 날림
//                        sseEmitter.complete();
                        break;
                    }
                    Thread.sleep(1000);
                }
//            } catch (IOException | InterruptedException e) {
            } catch (InterruptedException e) {
                sseEmitter.completeWithError(e);
            }
        });

        return sseEmitter;
    }
}
