package com.sparta.final_project.domain.ticket.service;

import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import com.sparta.final_project.domain.ticket.entity.BuyTickets;
import com.sparta.final_project.domain.ticket.entity.Ticket;
import com.sparta.final_project.domain.ticket.repository.BuyTicketsRepository;
import com.sparta.final_project.domain.ticket.repository.TicketRepository;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.repository.UserRepository;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class TicketBuyConsumer {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final BuyTicketsRepository buyTicketsRepository;

    public TicketBuyConsumer(TicketRepository ticketRepository, UserRepository userRepository, BuyTicketsRepository buyTicketsRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
        this.buyTicketsRepository = buyTicketsRepository;
    }

    @KafkaListener(topics = "buy-tickets-topic", groupId = "ticket-consumer-group")
    public void processBuyTicket(BuyTicketsRequest buyTicketsRequest) {
        // 티켓 구매 로직 처리
        Ticket ticket = ticketRepository.findById(buyTicketsRequest.getTicketId())
                .orElseThrow(() -> new IllegalArgumentException("해당 티켓을 찾을 수 없습니다."));

        User user = userRepository.findById(buyTicketsRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        if (ticket.getTicketCount() == 0) {
            throw new IllegalArgumentException("구매하려는 티켓의 수량이 부족합니다.");
        }

        ticket.setTicketCount(ticket.getTicketCount() - 1);

        BuyTickets buyTickets = new BuyTickets(buyTicketsRequest, ticket, user);
        buyTicketsRepository.save(buyTickets);
        ticketRepository.save(ticket);

        System.out.println("티켓 구매가 성공적으로 완료되었습니다.");
    }
}
