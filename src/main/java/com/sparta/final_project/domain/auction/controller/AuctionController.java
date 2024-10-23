package com.sparta.final_project.domain.auction.controller;

import com.sparta.final_project.config.AuthUser;
import com.sparta.final_project.domain.auction.dto.request.AuctionRequest;
import com.sparta.final_project.domain.auction.dto.response.AuctionResponse;
import com.sparta.final_project.domain.auction.service.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {

    private final AuctionService auctionService;

//    생성
    @PostMapping("item/{itemId}")
    public ResponseEntity<AuctionResponse> createAuction(AuthUser authUser, @PathVariable("itemId")Long itemId, @RequestBody AuctionRequest auctionRequest) {
        return ResponseEntity.ok().body(auctionService.createAuction(authUser,itemId,auctionRequest));
    }
//    단건조회
    @GetMapping("/user/{userId}/{auctionId}")
    public ResponseEntity<AuctionResponse> getAuction(@PathVariable("userId")Long userId,@PathVariable("auctionId") Long auctionId) {
        return ResponseEntity.ok().body(auctionService.getAuction(userId,auctionId));
    }
//    다건조회
    @GetMapping("/user/{userId}/auctions")
    public ResponseEntity<List<AuctionResponse>> getAuctionList(@PathVariable("userId")Long userId) {
        return ResponseEntity.ok().body(auctionService.getAuctionList(userId));
    }
//    수정
    @PutMapping("/user/{userId}/{auctionId}")
    public ResponseEntity<AuctionResponse> updateAuction(@PathVariable("userId")Long userId,@PathVariable("auctionId") Long auctionId, @RequestBody AuctionRequest auctionRequest) {
        return ResponseEntity.ok().body(auctionService.updateAuction(userId,auctionId, auctionRequest));
    }
//    삭제
    @DeleteMapping("/user/{userId}/{auctionId}")
    public ResponseEntity<String> deleteAuction(@PathVariable("userId")Long userId,@PathVariable("auctionId") Long auctionId) {
        auctionService.deleteAuction(userId,auctionId);
        return ResponseEntity.ok().body("경매가 삭제되었습니다.");
    }
}
