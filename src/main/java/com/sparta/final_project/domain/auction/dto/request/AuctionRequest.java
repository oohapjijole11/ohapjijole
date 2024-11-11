package com.sparta.final_project.domain.auction.dto.request;

import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.auction.entity.Status;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class AuctionRequest {

    private String title;
    private Integer startPrice = 0;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

}
