package com.sparta.final_project.domain.auction.repository;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    List<Auction> findAllByStatusAndStartTimeLessThan(Status status, LocalDateTime now);

    List<Auction> findAllByStatusAndEndTimeLessThan(Status status, LocalDateTime now);

    Auction findByIdAndStatus(Long auctionId, Status status);

}
