package com.sparta.final_project.domain.bid.entity;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.common.entity.Timestamped;
import com.sparta.final_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "bids")
public class Bid extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bid_id")
    private Long id;

    @Column(nullable = false)
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Bid(int price, User user, Auction auction) {
        this.price = price;
        this.auction = auction;
        this.user = user;
    }
}
