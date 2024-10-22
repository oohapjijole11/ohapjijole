package com.sparta.final_project.domain.ticket.dto;

import lombok.Data;

@Data
public class TicketMessageResponseDto {
    private String message;

    public TicketMessageResponseDto(String message) {
        this.message = message;
    }

    // Getter
    public String getMessage() {
        return message;
    }
}
