package com.sparta.final_project.domain.ticket.dto.request;

import com.sparta.final_project.domain.ticket.entity.Ticket;
import lombok.Getter;

@Getter
public class BuyTicketsRequest {
    private Long ticketId;
    private Long userId;
    private Long ticketNumber;
}
