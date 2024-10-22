package com.sparta.final_project.domain.ticket.entity;

import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.ticket.dto.request.TicketRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ticketId;

    @Column(nullable = false)
    private String ticketTitle;

    @Column(nullable = false)
    private String ticketDescription;

    @Column(nullable = false)
    private TicketStatus ticketStatus;

    @Column
    private Grade ticketGrade;

    @Column
    private Long ticketCount;

    public Ticket(TicketRequest ticketRequest) {
        this.ticketTitle = ticketRequest.getTicketTitle();
        this.ticketDescription = ticketRequest.getTicketDescription();
        this.ticketStatus = ticketRequest.getTicketStatus();
        this.ticketCount = ticketRequest.getTicketCount();
        this.ticketGrade = ticketRequest.getTicketGrade();
    }

}