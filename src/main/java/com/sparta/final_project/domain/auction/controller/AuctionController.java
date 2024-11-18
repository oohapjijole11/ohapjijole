package com.sparta.final_project.domain.auction.controller;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.auction.dto.request.AuctionRequest;
import com.sparta.final_project.domain.auction.dto.response.AuctionResponse;
import com.sparta.final_project.domain.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auctions")
public class AuctionController {

    private final AuctionService auctionService;

//    생성
    @PostMapping("/items/{itemId}")
    public ResponseEntity<AuctionResponse> createAuction(@AuthenticationPrincipal AuthUser authUser, @PathVariable("itemId")Long itemId, @RequestBody AuctionRequest auctionRequest) {
        return ResponseEntity.ok().body(auctionService.createAuction(authUser,itemId,auctionRequest));
    }
//    단건조회
    @GetMapping("/{auctionId}")
    public ResponseEntity<AuctionResponse> getAuction(@AuthenticationPrincipal AuthUser authUser,@PathVariable("auctionId") Long auctionId) {
        return ResponseEntity.ok().body(auctionService.getAuction(authUser,auctionId));
    }
//    다건조회
    @GetMapping
    public ResponseEntity<List<AuctionResponse>> getAuctionList(@AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok().body(auctionService.getAuctionList(authUser));
    }
//    수정
    @PutMapping("/{auctionId}")
    public ResponseEntity<AuctionResponse> updateAuction(@AuthenticationPrincipal AuthUser authUser,@PathVariable("auctionId") Long auctionId, @RequestBody AuctionRequest auctionRequest) {
        return ResponseEntity.ok().body(auctionService.updateAuction(authUser,auctionId, auctionRequest));
    }
//    삭제
    @DeleteMapping("/{auctionId}")
    public ResponseEntity<String> deleteAuction(@AuthenticationPrincipal AuthUser authUser,@PathVariable("auctionId") Long auctionId) {
        auctionService.deleteAuction(authUser,auctionId);
        return ResponseEntity.ok().body("경매가 삭제되었습니다.");
    }
}
