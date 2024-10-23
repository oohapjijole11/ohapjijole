package com.sparta.final_project.domain.ticket.dto.response;

import lombok.Data;

@Data

public class BuyTicketsResponse {
    private String message;

    public BuyTicketsResponse(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
