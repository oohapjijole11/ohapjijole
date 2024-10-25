package com.sparta.final_project.domain.bid.controller;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.bid.dto.response.SbidResponse;
import com.sparta.final_project.domain.bid.dto.response.SbidSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bid")
public class SbidController {
    private final com.sparta.final_project.domain.bid.service.SbidService sbidService;

    @PostMapping("/success/{auctionId}")
    public SbidResponse createSbid(@PathVariable Long auctionId) {
        return sbidService.createSbid(auctionId);
    }

    @GetMapping("/purchase")
    public Page<SbidSimpleResponse> getSbid(@AuthenticationPrincipal AuthUser authUser,
                                            @RequestParam(defaultValue = "1", required = false) int page,
                                            @RequestParam(defaultValue = "10", required = false) int size) {
        return sbidService.getSbids(authUser.getId(), page, size);
    }


}
