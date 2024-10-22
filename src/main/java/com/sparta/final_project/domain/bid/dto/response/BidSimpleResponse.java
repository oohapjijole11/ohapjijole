package com.sparta.final_project.domain.bid.dto.response;

import com.sparta.final_project.domain.bid.entity.Bid;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BidSimpleResponse {
    private Long id;
    private Integer price;
    private LocalDateTime createdAt;

    public BidSimpleResponse(Bid bid) {
        this.id = bid.getId();
        this.price = bid.getPrice();
        this.createdAt = bid.getCreatedAt();
    }
}
