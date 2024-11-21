package com.sparta.final_project.domain.ticket.dto.response;

import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.ticket.entity.Ticket;
import com.sparta.final_project.domain.ticket.entity.TicketStatus;
import lombok.Data;

@Data
public class TicketResponse {

    private String ticketTitle;
    private String ticketDescription;
    private TicketStatus ticketStatus;
    private Long ticketCount;
    private Grade ticketGrade;


    public TicketResponse(Ticket ticket) {
        this.ticketTitle = ticket.getTicketTitle();
        this.ticketDescription = ticket.getTicketDescription();
        this.ticketCount = ticket.getTicketCount();
        this.ticketGrade = ticket.getTicketGrade();
        this.ticketStatus = ticket.getTicketStatus();
    }
}
