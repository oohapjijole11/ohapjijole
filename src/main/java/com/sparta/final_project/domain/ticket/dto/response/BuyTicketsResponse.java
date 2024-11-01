package com.sparta.final_project.domain.ticket.dto.response;

import com.sparta.final_project.domain.ticket.entity.BuyTickets;
import com.sparta.final_project.domain.ticket.entity.Ticket;
import lombok.Data;

@Data
public class BuyTicketsResponse {
    Long buyTicketsId;

    public BuyTicketsResponse(BuyTickets buyTickets) {
        this.buyTicketsId = buyTickets.getId();
    }
}
