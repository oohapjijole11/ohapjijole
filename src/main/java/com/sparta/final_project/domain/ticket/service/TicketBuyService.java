package com.sparta.final_project.domain.ticket.service;

import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import com.sparta.final_project.domain.ticket.dto.response.BuyTicketsResponse;
import com.sparta.final_project.domain.ticket.entity.BuyTickets;
import com.sparta.final_project.domain.ticket.entity.Ticket;
import com.sparta.final_project.domain.ticket.repository.BuyTicketsRepository;
import com.sparta.final_project.domain.ticket.repository.TicketRepository;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class TicketBuyService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final BuyTicketsRepository buyTicketsRepository;
    private final KafkaTemplate<String, BuyTicketsRequest> kafkaTemplate;



    public void buyTicket(BuyTicketsRequest buyTicketsRequest) {

        Ticket ticket = ticketRepository.findById(buyTicketsRequest.getTicketId())
                .orElseThrow(() -> new IllegalArgumentException("해당 티켓을 찾을 수 없습니다."));
        // 사용자 찾기
        User user = userRepository.findById(buyTicketsRequest.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));
        // 티켓 수량 확인
        if (ticket.getTicketCount() == 0) {
            throw new IllegalArgumentException("구매하려는 티켓의 수량이 부족합니다.");
        }
        // 티켓 수량 감소
        ticket.setTicketCount(ticket.getTicketCount() - 1);

        BuyTickets buyTickets = new BuyTickets(buyTicketsRequest,ticket,user);

        buyTicketsRepository.save(buyTickets);
        ticketRepository.save(ticket);  // 티켓 수 업데이트
    }

    //구매 티켓 다건 조회
    public List<BuyTicketsResponse> getbuyticketList() {
        List<BuyTickets> buyticketList = buyTicketsRepository.findAll();
        return buyticketList.stream().map(BuyTicketsResponse::new).toList();
    }

}
