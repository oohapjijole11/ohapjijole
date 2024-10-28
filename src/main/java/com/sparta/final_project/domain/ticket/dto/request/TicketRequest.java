package com.sparta.final_project.domain.ticket.dto.request;

import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.ticket.entity.TicketStatus;
import lombok.Getter;

@Getter
public class TicketRequest {
    private String ticketTitle; //ticketTitle
    private String ticketDescription;
    private TicketStatus ticketStatus;
    private Grade ticketGrade;
    private Long ticketCount;

}