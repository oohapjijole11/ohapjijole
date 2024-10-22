package com.sparta.final_project.domain.bid.dto.response;

import com.sparta.final_project.domain.auction.dto.AuctionResponseDto;
import com.sparta.final_project.domain.bid.entity.Bid;
import com.sparta.final_project.domain.user.dto.response.UserResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BidResponse {
    private Long id;
    private Integer price;
    private UserResponse user;
    private AuctionResponseDto auction;
    private LocalDateTime createdAt;

    public BidResponse(Bid bid) {
        this.id = bid.getId();
        this.price = bid.getPrice();
        this.createdAt = bid.getCreatedAt();
        this.auction = new AuctionResponseDto(bid.getAuction());
        this.user = new UserResponse(bid.getUser().getId());
    }
}

