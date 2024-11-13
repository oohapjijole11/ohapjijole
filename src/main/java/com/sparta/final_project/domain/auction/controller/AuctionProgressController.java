package com.sparta.final_project.domain.auction.controller;

import com.sparta.final_project.domain.bid.service.BidCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auctions")
public class AuctionProgressController {

    private final BidCommonService commonService;

//    경매 시작 알림 메세지
    @PostMapping("/notify/{auctionId}")
    public ResponseEntity<String> notifyAuctionStart(@PathVariable Long auctionId) {
        commonService.startNotification(auctionId);
        return ResponseEntity.ok("Notification sent to auction " + auctionId);
    }
}
