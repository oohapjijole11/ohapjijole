package com.sparta.final_project.domain.ticket.repository;

import com.sparta.final_project.domain.ticket.entity.Ticket;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // 락 걸지 않았을 때
    Optional<Ticket> findById(Long ticketId);

    //비관적 락 로직
//    @Lock(LockModeType.PESSIMISTIC_WRITE)
//    @Query("SELECT t FROM Ticket t WHERE t.ticketId = :ticketId")
//    Optional<Ticket> findByIdWithLock(@Param("ticketId") Long ticketId);


}
