package com.sparta.final_project.domain.auction.controller;

import com.sparta.final_project.domain.auction.service.AuctionProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionProgressController {

    private final AuctionProgressService auctionProgressService;

    //    경매 시간체크
    @GetMapping(value = "/{auctionId}/countdown", produces = "text/event-stream")
    public SseEmitter getCountdown(@PathVariable Long auctionId,
                                   @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return auctionProgressService.startAuctionCountdown(auctionId,lastEventId);
    }
}
