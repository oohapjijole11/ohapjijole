package com.sparta.final_project.domain.ticket.service;
import com.sparta.final_project.domain.auction.dto.AuctionResponseDto;
import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.ticket.dto.TicketRequestDto;
import com.sparta.final_project.domain.ticket.dto.TicketResponseDto;
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
    public Ticket createticket(TicketRequestDto ticketRequestDto) {
        Ticket ticket = new Ticket(ticketRequestDto);
        Ticket savedTicket = ticketRepository.save(ticket);
        return savedTicket;
    }

    //티켓 단건조회
    public TicketResponseDto getTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(()
                -> new RuntimeException("없는 티켓입니다"));
        return new TicketResponseDto(ticket);
    }

    //티켓 다건 조회
    public List<TicketResponseDto> getticketList() {
        List<Ticket> ticketList = ticketRepository.findAll();
        return ticketList.stream().map(TicketResponseDto::new).toList();
    }

    //티켓 삭제
    public void deleteTicket(Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 경매입니다."));
        ticketRepository.delete(ticket);
    }

}
