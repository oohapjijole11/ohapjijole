package com.sparta.final_project.domain.ticket.dto;

import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.ticket.entity.Ticket;
import lombok.Data;

import java.time.LocalTime;

@Data
public class TicketResponseDto {
    private String ticketTitle;
    private String ticketDescription;
    private String ticketStatus;
    private String ticketCount;
    private Grade ticketGrade;
    private LocalTime ticketCreatedAt;

    public TicketResponseDto(Ticket ticket) {
        this.ticketTitle = ticket.getTicketTitle();
        this.ticketDescription = ticket.getTicketDescription();
        this.ticketStatus = ticket.getTicketStatus();
        this.ticketCount = ticket.getTicketCount();
        this.ticketGrade = ticket.getTicketGrade();
    }
}
