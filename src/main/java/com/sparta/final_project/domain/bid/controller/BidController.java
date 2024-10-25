package com.sparta.final_project.domain.bid.controller;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.bid.dto.request.BidRequest;
import com.sparta.final_project.domain.bid.dto.response.BidResponse;
import com.sparta.final_project.domain.bid.dto.response.BidSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bid")
public class BidController {
    private final com.sparta.final_project.domain.bid.service.BidService bidService;

    @PostMapping("")
    public ResponseEntity<BidResponse> createBid (@AuthenticationPrincipal AuthUser authUser, @RequestBody BidRequest request) {
        return ResponseEntity.ok(bidService.createBid(authUser.getId(), request));
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<BidSimpleResponse>> BidList (@PathVariable Long auctionId) {
        return ResponseEntity.ok(bidService.getBids(auctionId));
    }

}