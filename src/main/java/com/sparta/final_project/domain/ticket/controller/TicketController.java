package com.sparta.final_project.domain.ticket.controller;



import com.sparta.final_project.domain.ticket.dto.response.TicketMessageResponse;
import com.sparta.final_project.domain.ticket.dto.request.TicketRequest;
import com.sparta.final_project.domain.ticket.dto.response.TicketResponse;
import com.sparta.final_project.domain.ticket.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/auctions/ticket")
public class TicketController {

    private final TicketService ticketService;

    // 티켓 생성
    @PostMapping

    public ResponseEntity<TicketMessageResponse> createTicket(@RequestBody TicketRequest ticketRequestDto) {
        ticketService.createticket(ticketRequestDto);
        // 메시지를 담은 응답 생성
        TicketMessageResponse responseDto = new TicketMessageResponse("티켓이 성공적으로 생성되었습니다.");
        // 응답으로 메시지를 반환
        return ResponseEntity.ok(responseDto);
    }

    //티켓 단건조회
    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> getTicket(@PathVariable("ticketId") Long ticketId) {
        return ResponseEntity.ok().body(ticketService.getTicket(ticketId));
    }

    //티켓 다건조회
    @GetMapping("/ticketList")
    public ResponseEntity<List<TicketResponse>> getTicketList() {
        return ResponseEntity.ok().body(ticketService.getticketList());
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<String> deleteTicket(@PathVariable("ticketId") Long ticketId) {
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.ok().body("경매가 삭제되었습니다.");
    }
}
