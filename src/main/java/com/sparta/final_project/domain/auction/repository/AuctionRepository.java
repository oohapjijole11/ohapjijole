package com.sparta.final_project.domain.auction.repository;

import com.sparta.final_project.domain.auction.dto.AuctionResponseDto;
import com.sparta.final_project.domain.auction.entity.Auction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {
}
