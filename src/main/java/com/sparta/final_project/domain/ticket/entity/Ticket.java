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
    @Column(name = "ticket_id")
    private Long ticketId;

    @Column(nullable = false)
    private String ticketTitle;

    @Column(nullable = false)
    private String ticketDescription;

    @Enumerated(EnumType.STRING)
    @Column
    private TicketStatus ticketStatus;

    @Enumerated(EnumType.STRING)
    @Column
    private Grade ticketGrade;

    @Column
    private Long ticketCount;



    public Ticket(TicketRequest ticketRequestDto) {
        this.ticketTitle = ticketRequestDto.getTicketTitle();
        this.ticketDescription = ticketRequestDto.getTicketDescription();
        this.ticketStatus = ticketRequestDto.getTicketStatus();
        this.ticketCount = ticketRequestDto.getTicketCount();
        this.ticketGrade = ticketRequestDto.getTicketGrade();
    }


}
