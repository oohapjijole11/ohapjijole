package com.sparta.final_project.domain.bid.dto.response;

import com.sparta.final_project.domain.auction.dto.response.AuctionResponse;
import com.sparta.final_project.domain.auction.entity.Auction;
import lombok.Getter;

@Getter
public class FBidResponse extends EndBidResponse{
    private String message = "경매 유찰";
    private AuctionResponse auction;

    public FBidResponse (Auction auction) {
        this.auction = new AuctionResponse(auction);
    }
}
