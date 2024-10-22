package com.sparta.final_project.domain.bid.dto.response;

import com.sparta.final_project.domain.auction.dto.response.AuctionResponse;
import com.sparta.final_project.domain.bid.entity.Sbid;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SbidSimpleResponse {
    private Long id;
    private Integer price;
    private Integer charge;
    private AuctionResponse auction;
    private LocalDateTime createdAt;

    public SbidSimpleResponse(Sbid sbid) {
        this.id = sbid.getId();
        this.price = sbid.getPrice();
        this.charge = sbid.getCharge();
        this.auction = new AuctionResponse(sbid.getAuction());
        this.createdAt = sbid.getCreatedAt();
    }
}
