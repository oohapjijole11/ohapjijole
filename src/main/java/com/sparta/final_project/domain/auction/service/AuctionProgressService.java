package com.sparta.final_project.domain.auction.service;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.bid.entity.Bid;
import com.sparta.final_project.domain.bid.repository.BidRepository;
import com.sparta.final_project.domain.bid.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionProgressService {

    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;
    private final EmitterRepository emitters;

    //     경매 종료
    public void auctionEnds(Long auctionId) {
        LocalDateTime now = LocalDateTime.now();
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new RuntimeException("경매가 존재하지 않습니다."));
        // 경매 종료 시간 확인
        if (now.isAfter(auction.getEndTime())) {
            // 경매에 입찰자가 있는지 확인
            List<Bid> bids = bidRepository.findAllByAuctionOrderByCreatedAtDesc(auction);

            if (!bids.isEmpty()) {
                Bid lastBid = bids.get(0); // 마지막 입찰
                if (lastBid.getUser() != null) {
                    auction.setStatus(Status.SUCCESSBID);
                    sendAuctionResultMessage(auctionId, lastBid.getUser().getName() + "님이 경매에 낙찰되었습니다!");
                } else {
                    auction.setStatus(Status.FAILBID);
                    sendAuctionResultMessage(auctionId, "경매가 유찰 되었습니다.");
                }
            } else {
                auction.setStatus(Status.FAILBID);
                sendAuctionResultMessage(auctionId, "경매가 유찰 되었습니다."); // 입찰자가 없는 경우
            }

            auctionRepository.save(auction); // 변경된 경매 상태 저장
        }
    }


    private void sendAuctionResultMessage(Long auctionId, String message) {
        Map<String, SseEmitter> sseEmitters = emitters.findAllEmitterStartWithByAuctionId(String.valueOf(auctionId));
        sendToEmitters(sseEmitters, message);
    }

    private void sendToEmitters(Map<String, SseEmitter> sseEmitters, String message) {
        sseEmitters.forEach((key, emitter) -> {
            try {
                emitter.send(message);
            } catch (IOException e) {
                emitters.deleteById(key);
                log.error("Error sending to emitter: " + e.getMessage());
            }
        });


        //   AWS lambda 사용
        //        경매 시작
//    @Scheduled(cron = "0 * * * * *")
//    public void monitorAuctionStart() {
//        List<Auction> auctions = auctionRepository.findAllByStatus(Status.WAITING);
//        LocalDateTime now = LocalDateTime.now();
//        boolean updated = false;
//        for (Auction auction : auctions) {
//            if (now.isEqual(auction.getStartTime()) || now.isAfter(auction.getStartTime())) {
//                auction.setStatus(Status.BID);
//                commonService.startNotification(auction.getId());
//                updated = true;
//            }
//        }
//        if (updated) {
//            auctionRepository.saveAll(auctions);
//        }
//    }

    }
}