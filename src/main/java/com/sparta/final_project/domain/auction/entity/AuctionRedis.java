package com.sparta.final_project.domain.auction.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@RedisHash("AuctionRedis")
public class AuctionRedis implements Serializable {

    @Id
    private Long id;
    private Status status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;


    public AuctionRedis(Auction auction) {
        this.id = auction.getId();
        this.status = auction.getStatus();
        this.startTime = auction.getStartTime();
        this.endTime = auction.getEndTime();
    }
}
