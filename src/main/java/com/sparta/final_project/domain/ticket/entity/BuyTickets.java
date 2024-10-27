package com.sparta.final_project.domain.ticket.entity;

import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import com.sparta.final_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "buytickets")
public class BuyTickets {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "buytickets_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_id")
    private Ticket ticket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private Long ticketnumber;


    public BuyTickets(BuyTicketsRequest buyTicketsRequest, Ticket ticket, User user) {
        this.ticket = ticket;
        this.user = user;
        this.ticketnumber = buyTicketsRequest.getTicketNumber();
    }
}
