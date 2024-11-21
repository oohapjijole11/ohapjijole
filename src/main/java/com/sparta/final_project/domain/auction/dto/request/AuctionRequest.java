package com.sparta.final_project.domain.auction.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class AuctionRequest {

    @NotBlank
    private String title;
    @NotBlank
    private Integer startPrice = 0;
    @NotBlank
    private LocalDateTime startTime;
    @NotBlank
    private LocalDateTime endTime;

}
