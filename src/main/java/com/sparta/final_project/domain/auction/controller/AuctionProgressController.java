package com.sparta.final_project.domain.auction.controller;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.auction.service.AuctionProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionProgressController {

    private final AuctionProgressService auctionProgressService;

    //    경매 시간체크
    @GetMapping("/{auctionId}/countdown")
    public SseEmitter getCountdown(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long auctionId) {
        return auctionProgressService.startAuctionCountdown(authUser, auctionId);
    }

//    경매 시작
    @GetMapping("/{auctionId}/auctionStart")
    public SseEmitter getAuction(@AuthenticationPrincipal AuthUser authUser, @PathVariable Long auctionId) {
        return auctionProgressService.monitorAuctionStart(authUser, auctionId);
    }
}
