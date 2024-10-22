package com.sparta.final_project.domain.auction.dto;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.auction.entity.Status;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class AuctionResponseDto {

    private int startPrice;
    private LocalTime startTime;
    private LocalTime endTime;
    private Grade grade;
    private Status status;

    public AuctionResponseDto(Auction auction) {

        this.startPrice = auction.getStartPrice();
        this.startTime = auction.getStartTime();
        this.endTime = auction.getEndTime();
        this.grade = auction.getGrade();
        this.status = auction.getStatus();
    }
}
