package com.sparta.final_project.domain.bid.controller;

import com.sparta.final_project.domain.bid.dto.request.BidRequest;
import com.sparta.final_project.domain.bid.dto.response.BidResponse;
import com.sparta.final_project.domain.bid.dto.response.BidSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bid")
public class BidController {
    private final com.sparta.final_project.domain.bid.service.BidService bidService;

    @PostMapping("/{userId}")
    public ResponseEntity<BidResponse> createBid (@PathVariable Long userId, @RequestBody BidRequest request) {
        return ResponseEntity.ok(bidService.createBid(userId, request));
    }

    @GetMapping("/auction/{auctionId}")
    public ResponseEntity<List<BidSimpleResponse>> BidList (@PathVariable Long auctionId) {
        return ResponseEntity.ok(bidService.getBids(auctionId));
    }

}