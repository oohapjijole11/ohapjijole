package com.sparta.final_project.domain.auction.service;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionSchedulerService {
    private final AuctionRepository auctionRepository;

    @Scheduled(fixedRate = 1000)
    public void checkAuctionStatus() {
        LocalDateTime now = LocalDateTime.now();

//        List<Auction> auctionToStart = auctionRepository.findByStatusAndStartTimeLessThan(Status.WAITING, now);
//        auctionToStart.forEach(this::startAuction);
//        List<Auction> auctionToEnd = auctionRepository.findByStatusAndEndTimeLessThan(Status.BID, now);
//        auctionToEnd.forEach(this::endAuction);

    }

//    경매 시작
    private void startAuction(Auction auction) {
        auction.setStatus(Status.BID);
        auctionRepository.save(auction);

    }

//    경매 마감
    private void endAuction(Auction auction) {
        auction.setStatus(Status.SUCCESSBID);
        auctionRepository.save(auction);

    }

}
