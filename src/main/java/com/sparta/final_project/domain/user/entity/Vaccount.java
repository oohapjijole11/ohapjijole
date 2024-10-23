package com.sparta.final_project.domain.user.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Vaccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long vaccount_id;

    private Long balance;

    @OneToOne(fetch = FetchType.LAZY)
    private User user;

}
