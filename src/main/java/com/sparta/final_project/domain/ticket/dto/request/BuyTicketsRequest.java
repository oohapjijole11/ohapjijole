package com.sparta.final_project.domain.ticket.dto.request;

import com.sparta.final_project.domain.ticket.entity.Ticket;
import lombok.Getter;

@Getter
public class BuyTicketsRequest {
    private Long ticketId;
    private Long userId;
    private Long ticketNumber;

    public BuyTicketsRequest(Long ticketId, Long userId, Long ticketNumber) {
        this.ticketId = ticketId;
        this.userId = userId;
        this.ticketNumber = ticketNumber;
    }
    public BuyTicketsRequest() {
    }

    // Getters and setters
    public Long getTicketId() {
        return ticketId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getTicketNumber() {
        return ticketNumber;
    }


}
