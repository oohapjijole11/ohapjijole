package com.sparta.final_project.domain.ticket.entity;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.ticket.dto.request.TicketRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ticket_id")
    private Long id;

    @Column(nullable = false)
    private String ticketTitle;

    @Column(nullable = false)
    private String ticketDescription;

    @Column(nullable = false)
    private TicketStatus ticketStatus;

    @Column
    private Long ticketCount;

    @Column
    @Enumerated(EnumType.STRING)
    private Grade ticketGrade;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "auction_id")
    private Auction auction;


    @Version
    private Long version;

    public Ticket(TicketRequest ticketRequest, Auction auction) {
        this.ticketTitle = ticketRequest.getTicketTitle();
        this.ticketDescription = ticketRequest.getTicketDescription();
        this.ticketStatus = ticketRequest.getTicketStatus();
        this.ticketCount = ticketRequest.getTicketCount();
        this.ticketGrade = ticketRequest.getTicketGrade();
        this.auction = auction;
    }

}
