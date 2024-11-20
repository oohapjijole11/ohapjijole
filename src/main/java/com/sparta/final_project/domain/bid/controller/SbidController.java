package com.sparta.final_project.domain.bid.controller;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.bid.dto.response.EndBidResponse;
import com.sparta.final_project.domain.bid.dto.response.SbidSimpleResponse;
import com.sparta.final_project.domain.bid.service.SbidService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sbids")
public class SbidController {
    private final SbidService sbidService;

    //낙찰
    @PostMapping("/success/{auctionId}")
    public EndBidResponse createSbid(@PathVariable Long auctionId) {
        return sbidService.createSbid(auctionId);
    }

    //내가 낙찰한 경매 목록
    @GetMapping("/purchase")
    public Page<SbidSimpleResponse> getSbid(@AuthenticationPrincipal AuthUser authUser,
                                            @RequestParam(defaultValue = "1", required = false) int page,
                                            @RequestParam(defaultValue = "10", required = false) int size) {
        return sbidService.getSbids(authUser.getId(), page, size);
    }


}
