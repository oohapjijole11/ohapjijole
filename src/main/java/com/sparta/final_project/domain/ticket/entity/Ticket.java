package com.sparta.final_project.domain.ticket.entity;

import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.ticket.dto.TicketRequestDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
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

    @Column(nullable = false)
    private LocalTime createdAt;

    @Column
    private Grade ticketGrade;

    @Column
    private String ticketCount;

    public Ticket(TicketRequestDto ticketRequestDto) {
        this.ticketTitle = ticketRequestDto.getTicketTitle();
        this.ticketDescription = ticketRequestDto.getTicketDescription();
        this.ticketStatus = ticketRequestDto.getTicketStatus();
        this.ticketCount = ticketRequestDto.getTicketCount();
        this.ticketGrade = ticketRequestDto.getTicketGrade();
    }

}
