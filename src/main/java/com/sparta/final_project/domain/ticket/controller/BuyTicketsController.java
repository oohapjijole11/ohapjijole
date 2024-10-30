package com.sparta.final_project.domain.ticket.controller;

import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import com.sparta.final_project.domain.ticket.dto.response.BuyTicketsResponse;
import com.sparta.final_project.domain.ticket.service.SqsService;
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
    private final SqsService sqsService;
    // 티켓 구매 요청
    @PostMapping
    public ResponseEntity<String> buyTicket(@RequestBody BuyTicketsRequest buyTicketsRequest) {
        // SQS로 메시지 전송
        sqsService.sendMessage(buyTicketsRequest);
        return ResponseEntity.ok("티켓 구매 요청이 SQS에 전송되었습니다."); // 응답 메시지
    }

    //티켓 다건조회
    @GetMapping("/buyticketList")
    public ResponseEntity<List<BuyTicketsResponse>> getbuyTicketList() {
        return ResponseEntity.ok().body(ticketBuyService.getbuyticketList());
    }
}
