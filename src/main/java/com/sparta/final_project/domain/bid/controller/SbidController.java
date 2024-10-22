package com.sparta.final_project.domain.bid.controller;

import com.sparta.final_project.domain.bid.dto.response.SbidResponse;
import com.sparta.final_project.domain.bid.dto.response.SbidSimpleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bid")
public class SbidController {
    private final com.sparta.final_project.domain.bid.service.SbidService sbidService;

    @PostMapping("/success/{auctionId}/{userId}")
    public SbidResponse createSbid(@PathVariable Long userId, @PathVariable Long auctionId) {
        return sbidService.createSbid(userId, auctionId);
    }

    @GetMapping("/purchase/{userId}")
    public Page<SbidSimpleResponse> getSbid(@PathVariable Long userId,
                                            @RequestParam(defaultValue = "1", required = false) int page,
                                            @RequestParam(defaultValue = "10", required = false) int size) {
        return sbidService.getSbids(userId, page, size);
    }


}
