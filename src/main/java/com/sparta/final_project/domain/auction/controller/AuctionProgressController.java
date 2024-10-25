package com.sparta.final_project.domain.auction.controller;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.auction.service.AuctionService;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuctionProgressController {

    private final AuctionService auctionService;
    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;

    @GetMapping("/auction/start/{auctionId}")
    public String startAuction(@PathVariable Long auctionId, @AuthenticationPrincipal AuthUser authUser) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
        userRepository.findById(authUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (auction.getStatus() == Status.WAITING) {
            auction.setStatus(Status.BID);
            auctionRepository.save(auction);
            return "Auction started!";
        }
        return "Auction cannot be started, current status: " + auction.getStatus();
    }

    @GetMapping("/auction/{auctionId}/remaining-time")
    public long getRemainingTime(@PathVariable Long auctionId, @AuthenticationPrincipal AuthUser authUser) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));
        userRepository.findById(authUser.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return auctionService.getRemainingTime(auction);
    }

    // WebSocket endpoint to broadcast remaining time for all connected clients
    @MessageMapping("/app/auctionTimer/{auctionId}")
    @SendTo("/topic/auctionTimer")
    public long broadcastRemainingTime(@PathVariable("auctionId") Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId)
                .orElseThrow(() -> new RuntimeException("Auction not found"));

        return auctionService.getRemainingTime(auction);
    }
}
