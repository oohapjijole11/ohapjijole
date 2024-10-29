package com.sparta.final_project.domain.ticket.controller;

import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import com.sparta.final_project.domain.ticket.dto.response.BuyTicketsResponse;
import com.sparta.final_project.domain.ticket.service.TicketBuyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auction/buyticket")
public class BuyTicketsController {
    private final TicketBuyService ticketBuyService;
    // 티켓 구매 요청
    @PostMapping
    public ResponseEntity<String> buyTicket(@RequestBody BuyTicketsRequest buyTicketsRequest) {
        String responseMessage = ticketBuyService.buyTicket(buyTicketsRequest);
        // 응답으로 메시지를 반환
        return ResponseEntity.ok(responseMessage);
    }

    //티켓 다건조회
    @GetMapping("/buyticketList")
    public ResponseEntity<List<BuyTicketsResponse>> getTicketList() {
        return ResponseEntity.ok().body(ticketBuyService.getbuyticketList());
    }
}
