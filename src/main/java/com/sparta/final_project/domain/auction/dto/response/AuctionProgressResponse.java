package com.sparta.final_project.domain.auction.dto.response;

import com.sparta.final_project.domain.auction.entity.Auction;
import lombok.Getter;

@Getter
public class AuctionProgressResponse {

    private Auction auction;
    private Long remainingTime;

    public AuctionProgressResponse(Auction auction, long remainingTime) {
        this.auction = auction;
        this.remainingTime = remainingTime;
    }
}
