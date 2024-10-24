package com.sparta.final_project.domain.bid.service;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.bid.dto.response.SbidResponse;
import com.sparta.final_project.domain.bid.dto.response.SbidSimpleResponse;
import com.sparta.final_project.domain.bid.entity.Sbid;
import com.sparta.final_project.domain.bid.repository.BidRepository;
import com.sparta.final_project.domain.bid.repository.SbidRepository;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SbidService {
    private final SbidRepository sbidRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;
    private final BidRepository bidRepository;

    @Transactional
    public SbidResponse createSbid(Long userId, Long auctionId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(()-> new OhapjijoleException(ErrorCode._NOT_FOUND_AUCTION));
        int maxPrice = bidRepository.findAllByAuctionOrderByCreatedAtDesc(auction).get(0).getPrice();
        auction.bidSuccess(Status.SUCCESSBID, LocalDateTime.now());
        Sbid sbid = new Sbid(user, auction,maxPrice);
        Sbid saveSbid = sbidRepository.save(sbid);
        return new SbidResponse(saveSbid);
    }

    public Page<SbidSimpleResponse> getSbids(Long userId, int page, int size) {
        userRepository.findById(userId).orElseThrow(()-> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        Pageable pageable = PageRequest.of(page-1, size);
        Page<Sbid> sbids = sbidRepository.findAllByUserIdOrderByCreatedAtDesc(userId, pageable);
        return sbids.map(SbidSimpleResponse::new);
    }
}
