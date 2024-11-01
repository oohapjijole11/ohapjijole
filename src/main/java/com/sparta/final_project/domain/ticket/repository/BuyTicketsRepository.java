package com.sparta.final_project.domain.ticket.repository;

import com.sparta.final_project.domain.ticket.entity.BuyTickets;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface BuyTicketsRepository extends JpaRepository<BuyTickets, Long> {
    long countByTicketId(Long ticketId);
}
