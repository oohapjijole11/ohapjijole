package com.sparta.final_project.domain.bid.controller;

import com.sparta.final_project.domain.bid.service.BidService;
import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.bid.dto.request.BidRequest;
import com.sparta.final_project.domain.bid.dto.response.BidResponse;
import com.sparta.final_project.domain.bid.dto.response.BidSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bids")
public class BidController {
    private final BidService bidService;

    //경매장 입장
    @GetMapping(value = "/auctions/{auctionId}", produces = "text/event-stream")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<SseEmitter> subscribe(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long auctionId,
            // @RequestHeader를 이용하여 header를 받아 데이터를 꺼내서 사용
            @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return ResponseEntity.ok(bidService.subscribe(authUser,auctionId, lastEventId));
    }

    //입찰
    @PostMapping("/auctions/{auctionId}")
    public ResponseEntity<BidResponse> createBid (@AuthenticationPrincipal AuthUser authUser,
                                                  @PathVariable Long auctionId,
                                                  @RequestBody BidRequest request) {
        return ResponseEntity.ok(bidService.createBid(authUser.getId(), auctionId,request));
    }

    //입찰 목록 조회
    @GetMapping("/bids/auctions/{auctionId}")
    public ResponseEntity<List<BidSimpleResponse>> BidList (@PathVariable Long auctionId) {
        return ResponseEntity.ok(bidService.getBids(auctionId));
    }
}