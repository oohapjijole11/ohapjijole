package com.sparta.final_project.domain.ticket.controller;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import com.sparta.final_project.domain.ticket.dto.response.BuyTicketsResponse;
import com.sparta.final_project.domain.ticket.service.TicketBuyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auctions/buytickets")
public class BuyTicketsController {
    private final TicketBuyService ticketBuyService;

    // 티켓 구매 요청
    @PostMapping
    public ResponseEntity<String> buyTicket(@AuthenticationPrincipal AuthUser authUser, @RequestBody BuyTicketsRequest buyTicketsRequest) {
        // 티켓 구매 요청 처리
        String responseMessage = ticketBuyService.buyTicket(authUser, buyTicketsRequest);

        // 대기열 상태를 실시간으로 구독할 수 있도록 처리 (구독 상태 활성화)
        ticketBuyService.subscribeQueueStatus(authUser.getId());

        // 응답으로 메시지를 반환
        return ResponseEntity.ok(responseMessage);
    }

    //티켓 다건조회
    @GetMapping
    public ResponseEntity<List<BuyTicketsResponse>> getTicketList() {
        return ResponseEntity.ok().body(ticketBuyService.getbuyticketList());
    }
}
