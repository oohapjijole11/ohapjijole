package com.sparta.final_project.domain.bid.dto.response;

import com.sparta.final_project.domain.auction.dto.AuctionResponseDto;
import com.sparta.final_project.domain.bid.entity.Sbid;
import com.sparta.final_project.domain.user.dto.response.UserResponse;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class SbidResponse {
    private Long id;
    private Integer price;
    private Integer charge;
    private UserResponse user;
    private AuctionResponseDto auction;
    private LocalDateTime createdAt;

    public SbidResponse(Sbid sbid) {
        this.id = sbid.getId();
        this.price = sbid.getPrice();
        this.charge = sbid.getCharge();
        this.auction = new AuctionResponseDto(sbid.getAuction());
        this.user = new UserResponse(sbid.getUser().getId());
        this.createdAt = sbid.getCreatedAt();
    }
}
