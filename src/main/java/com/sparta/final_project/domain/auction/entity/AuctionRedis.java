package com.sparta.final_project.domain.auction.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@RedisHash("AuctionRedis")
public class AuctionRedis implements Serializable {

    @Id
    private Long id;
    private Status status;
    private String startTime;
    private String endTime;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public AuctionRedis(Auction auction) {
        this.id = auction.getId();
        this.status = auction.getStatus();
        this.startTime = auction.getStartTime().format(formatter);
        this.endTime = auction.getEndTime().format(formatter);
    }

    public LocalDateTime getStartTimeAsLocalDateTime() {
        return LocalDateTime.parse(this.startTime, formatter);
    }

    public LocalDateTime getEndTimeAsLocalDateTime() {
        return LocalDateTime.parse(this.endTime, formatter);
    }
}
