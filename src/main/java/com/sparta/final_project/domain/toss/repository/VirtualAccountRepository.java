package com.sparta.final_project.domain.toss.repository;

import com.sparta.final_project.domain.toss.entity.VirtualAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VirtualAccountRepository extends JpaRepository<VirtualAccount, Long> {
    boolean existsByAccountNumber(String accountNumber);

}
