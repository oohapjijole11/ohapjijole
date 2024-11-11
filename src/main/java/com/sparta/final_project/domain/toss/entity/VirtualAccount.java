package com.sparta.final_project.domain.toss.entity;

import com.sparta.final_project.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "virtual_account")
public class VirtualAccount  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "virtual_account_id")
    private Long id;

    private String accountNumber;

    private Double balance = 0.0;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

}
