package com.sparta.final_project.domain.bid.dto.response;

import com.sparta.final_project.domain.bid.entity.Bid;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BidResponse {
    private Long id;
    private Integer price;
    private Long user;
    private Long auction;
    private LocalDateTime createdAt;

    public BidResponse(Bid bid) {
        this.id = bid.getId();
        this.price = bid.getPrice();
        this.createdAt = bid.getCreatedAt();
        this.auction = bid.getAuction().getId();
        this.user = bid.getUser().getId();
    }

}

