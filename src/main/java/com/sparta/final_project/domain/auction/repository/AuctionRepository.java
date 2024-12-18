package com.sparta.final_project.domain.auction.repository;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AuctionRepository extends JpaRepository<Auction, Long> {

    Auction findByIdAndStatus(Long auctionId, Status status);
    Auction findByItemIdAndStatusIn(Long itemId, List<Status> status);

}
