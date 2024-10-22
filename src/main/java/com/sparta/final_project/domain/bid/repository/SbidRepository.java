package com.sparta.final_project.domain.bid.repository;

import com.sparta.final_project.domain.bid.entity.Sbid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SbidRepository extends JpaRepository<Sbid, Long> {
    Page<Sbid> findAllByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
