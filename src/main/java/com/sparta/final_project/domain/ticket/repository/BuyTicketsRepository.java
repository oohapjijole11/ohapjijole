package com.sparta.final_project.domain.ticket.repository;

import com.sparta.final_project.domain.ticket.entity.BuyTickets;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BuyTicketsRepository extends JpaRepository<BuyTickets, Long> {

    List<BuyTickets> findAllByTicketId(Long ticketId);
}
