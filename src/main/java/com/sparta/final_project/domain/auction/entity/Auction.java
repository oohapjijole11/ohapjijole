package com.sparta.final_project.domain.auction.entity;

import com.sparta.final_project.domain.auction.dto.AuctionRequestDto;
import com.sparta.final_project.domain.auction.dto.AuctionResponseDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionId;

    @Column(nullable = false)
    private int startPrice;

    @Column(nullable = false)
    private LocalTime startTime;

    @Column(nullable = false)
    private LocalTime endTime;

    private Grade grade;

    private Status status;

//    @ManyToOne(fetch = FetchType.EAGER ,cascade = CascadeType.REMOVE)
//    @JoinColumn(name = "item_id")
//    private Item item;

    public Auction(AuctionRequestDto auctionRequestDto) {
        this.startPrice = auctionRequestDto.getStartPrice();
        this.startTime = auctionRequestDto.getStartTime();
        this.endTime = auctionRequestDto.getEndTime();
        this.grade = auctionRequestDto.getGrade();
        this.status = Status.BID;
    }


    public void update(AuctionRequestDto auctionRequestDto) {
        this.startPrice = auctionRequestDto.getStartPrice();
        this.startTime = auctionRequestDto.getStartTime();
        this.endTime = auctionRequestDto.getEndTime();
    }
}
