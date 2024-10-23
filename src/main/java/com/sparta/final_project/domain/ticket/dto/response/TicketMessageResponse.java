package com.sparta.final_project.domain.ticket.dto.response;

import lombok.Data;

@Data
public class TicketMessageResponse {
    private String message;

    public TicketMessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
