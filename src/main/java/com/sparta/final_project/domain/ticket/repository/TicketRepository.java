package com.sparta.final_project.domain.ticket.repository;

import com.sparta.final_project.domain.ticket.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
