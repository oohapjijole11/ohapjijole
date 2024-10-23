package com.sparta.final_project.domain.auction.dto.request;

import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.auction.entity.Status;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
public class AuctionRequest {

    private int startPrice = 0;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Grade grade;
    private Status status;
}
