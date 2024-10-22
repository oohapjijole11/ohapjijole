package com.sparta.final_project.domain.ticket.dto;

import com.sparta.final_project.domain.auction.entity.Grade;
import lombok.Data;
import lombok.Getter;

@Getter
public class TicketRequestDto {
    private String ticketTitle;
    private String ticketDescription;
    private String ticketStatus;
    private Grade ticketGrade;
    private String ticketCount;
}
