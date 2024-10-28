package com.sparta.final_project.domain.auction.dto.response;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.auction.entity.Status;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuctionResponse {

    private Long id;
    private int startPrice;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Grade grade;
    private Status status;

    public AuctionResponse(Auction auction) {
        this.id = auction.getId();
        this.startPrice = auction.getStartPrice();
        this.startTime = auction.getStartTime();
        this.endTime = auction.getEndTime();
        this.grade = auction.getGrade();
        this.status = auction.getStatus();
    }
}
