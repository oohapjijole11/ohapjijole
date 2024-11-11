package com.sparta.final_project.domain.auction.controller;

import com.sparta.final_project.domain.auction.service.AuctionProgressService;
import com.sparta.final_project.domain.bid.service.BidCommonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionProgressController {

    private final AuctionProgressService auctionProgressService;
    private final BidCommonService commonService;

    //    경매 시간체크
    @GetMapping(value = "/{auctionId}/countdown", produces = "text/event-stream")
    public SseEmitter getCountdown(@PathVariable Long auctionId,
                                   @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return auctionProgressService.startAuctionCountdown(auctionId,lastEventId);
    }

//    경매 시작 알림 메세지
    @PostMapping("/auction/notify/{auctionId}")
    public ResponseEntity<String> notifyAuctionStart(@PathVariable Long auctionId) {
        commonService.startNotification(auctionId);
        return ResponseEntity.ok("Notification sent to auction " + auctionId);
    }
}
