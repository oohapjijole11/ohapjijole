package com.sparta.final_project.domain.auction.dto.request;

import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.auction.entity.Status;
import lombok.Data;

import java.time.LocalTime;

@Data
public class AuctionRequest {

    private int startPrice = 0;
    private LocalTime startTime;
    private LocalTime endTime;
    private Grade grade;
    private Status status;
}
