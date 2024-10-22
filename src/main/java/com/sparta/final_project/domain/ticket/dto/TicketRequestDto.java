package com.sparta.final_project.domain.ticket.dto;

import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.ticket.entity.TicketStatus;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class TicketRequestDto {
    private String ticketTitle;
    private String ticketDescription;
    private TicketStatus ticketStatus;
    private Grade ticketGrade;
    private Long ticketCount;

}
