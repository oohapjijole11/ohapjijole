package com.sparta.final_project.domain.ticket.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sparta.final_project.domain.ticket.entity.Ticket;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BuyTicketsRequest {
    private Long ticketId;
    private Long userId;


}
