package com.sparta.final_project.domain.ticket.controller;

import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import com.sparta.final_project.domain.ticket.dto.request.TicketRequest;
import com.sparta.final_project.domain.ticket.dto.response.BuyTicketsResponse;
import com.sparta.final_project.domain.ticket.dto.response.TicketMessageResponse;
import com.sparta.final_project.domain.ticket.service.BuyTicketsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auction/buyticket")
public class BuyTicketsController {
    private final BuyTicketsService buyTicketsService;
    
    // 티켓 생성
    @GetMapping
    public ResponseEntity<TicketMessageResponse> buyTicket(@RequestBody BuyTicketsRequest buyTicketsRequest) {
        buyTicketsService.buyTicket(buyTicketsRequest);
        // 메시지를 담은 응답 생성
        TicketMessageResponse responseDto = new TicketMessageResponse("티켓이 성공적으로 생성되었습니다.");
        // 응답으로 메시지를 반환
        return ResponseEntity.ok(responseDto);
    }
}
