package com.sparta.final_project.domain.ticket.dto.response;

import lombok.Data;

@Data

public class BuyTicketsMessageResponse {
    private String message;

    public BuyTicketsMessageResponse(String message) {

        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
