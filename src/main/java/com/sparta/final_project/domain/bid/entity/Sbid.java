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
@Table(name = "sbids")
public class Sbid extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="bid_id")
    private Long id;

    @Column(nullable = false)
    private Integer price;

    @Column(nullable = false)
    private Integer charge;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Sbid(User user, Auction auction, int price) {
        this.user = user;
        this.auction = auction;
        this.price = price;
        this.charge = price/10;
    }
}
