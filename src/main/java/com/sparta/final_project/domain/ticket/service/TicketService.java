package com.sparta.final_project.domain.ticket.service;
import com.sparta.final_project.domain.ticket.dto.request.TicketRequest;
import com.sparta.final_project.domain.ticket.dto.response.TicketResponse;
import com.sparta.final_project.domain.ticket.entity.Ticket;
import com.sparta.final_project.domain.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;

    //티켓 생성
    public Ticket createticket(TicketRequest ticketRequest) {
        Ticket ticket = new Ticket(ticketRequest);
        Ticket savedTicket = ticketRepository.save(ticket);
        return savedTicket;
    }

    //티켓 단건조회
    public TicketResponse getTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(()
                -> new RuntimeException("없는 티켓입니다"));
        return new TicketResponse(ticket);
    }

    //티켓 다건 조회
    public List<TicketResponse> getticketList() {
        List<Ticket> ticketList = ticketRepository.findAll();
        return ticketList.stream().map(TicketResponse::new).toList();
    }

    //티켓 삭제
    public void deleteTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 티켓입니다."));
        ticketRepository.delete(ticket);
    }

}
