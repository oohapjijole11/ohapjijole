package com.sparta.final_project.domain.ticket.service;

import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import com.sparta.final_project.domain.ticket.dto.response.BuyTicketsResponse;
import com.sparta.final_project.domain.ticket.entity.BuyTickets;
import com.sparta.final_project.domain.ticket.entity.Ticket;
import com.sparta.final_project.domain.ticket.repository.TicketRepository;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class BuyTicketsService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;

    @Transactional
    public String buyTicket(BuyTicketsRequest buyTicketsRequest) {
        // 티켓 찾기
        Ticket ticket = ticketRepository.findById(buyTicketsRequest.getTicketId())
                .orElseThrow(() -> new IllegalArgumentException("해당 티켓을 찾을 수 없습니다."));

        // 사용자 찾기
        User user = userRepository.findById(buyTicketsRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        // 티켓 수량 확인
        if (ticket.getTicketCount() ==0) {
            throw new IllegalArgumentException("구매하려는 티켓의 수량이 부족합니다.");
        }

        // 티켓 수량 감소
        ticket.setTicketCount(ticket.getTicketCount() - 1);

        // TODO: 구매 내역 저장 (예: PurchaseHistory 엔티티에 저장)

        ticketRepository.save(ticket);  // 티켓 수 업데이트
        return "티켓 구매가 성공적으로 완료되었습니다.";
    }
}