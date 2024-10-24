package com.sparta.final_project.domain.auction.dto.request;

import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.auction.entity.Status;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
public class AuctionRequest {

    private Integer startPrice = 0;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Grade grade;
    private Status status;
}
