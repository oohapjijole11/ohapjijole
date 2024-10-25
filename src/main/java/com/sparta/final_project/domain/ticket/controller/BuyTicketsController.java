package com.sparta.final_project.domain.ticket.controller;

import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import com.sparta.final_project.domain.ticket.dto.response.BuyTicketsResponse;
import com.sparta.final_project.domain.ticket.service.TicketBuyConsumer;
import com.sparta.final_project.domain.ticket.service.TicketBuyProducer;
import com.sparta.final_project.domain.ticket.service.TicketBuyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auction/buyticket")
public class BuyTicketsController {
    private final TicketBuyService ticketBuyService;
    private final TicketBuyProducer ticketBuyProducer;
    private final TicketBuyConsumer ticketBuyConsumer;
    // 티켓 구매 요청
    @GetMapping
    public ResponseEntity<String> buyTicket(@RequestBody BuyTicketsRequest buyTicketsRequest) {
        ticketBuyProducer.sendBuyRequest(buyTicketsRequest);
        // 응답으로 메시지를 반환
        return ResponseEntity.ok("티켓 구매 요청이 성공적으로 접수되었습니다.");
    }

    //티켓 다건조회
    @GetMapping("/buyticketList")
    public ResponseEntity<List<BuyTicketsResponse>> getTicketList() {
        return ResponseEntity.ok().body(ticketBuyService.getbuyticketList());
    }
}
