package com.sparta.final_project.domain.auction.controller;

import com.sparta.final_project.domain.auction.dto.AuctionRequestDto;
import com.sparta.final_project.domain.auction.dto.AuctionResponseDto;
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
    @PostMapping
    public ResponseEntity<AuctionResponseDto> createAuction(@RequestBody AuctionRequestDto auctionRequestDto) {
        return ResponseEntity.ok().body(auctionService.createAuction(auctionRequestDto));
    }
//    단건조회
    @GetMapping("/{auctionId}")
    public ResponseEntity<AuctionResponseDto> getAuction(@PathVariable("auctionId") Long auctionId) {
        return ResponseEntity.ok().body(auctionService.getAuction(auctionId));
    }
//    다건조회
    @GetMapping("/auctionList")
    public ResponseEntity<List<AuctionResponseDto>> getAuctionList() {
        return ResponseEntity.ok().body(auctionService.getAuctionList());
    }
//    수정
    @PutMapping("/{auctionId}")
    public ResponseEntity<AuctionResponseDto> updateAuction(@PathVariable("auctionId") Long auctionId, @RequestBody AuctionRequestDto auctionRequestDto) {
        return ResponseEntity.ok().body(auctionService.updateAuction(auctionId, auctionRequestDto));
    }
//    삭제
    @DeleteMapping("/{auctionId}")
    public ResponseEntity<String> deleteAuction(@PathVariable("auctionId") Long auctionId) {
        auctionService.deleteAuction(auctionId);
        return ResponseEntity.ok().body("경매가 삭제되었습니다.");
    }
}
