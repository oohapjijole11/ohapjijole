package com.sparta.final_project.domain.bid.service;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.bid.dto.request.BidRequest;
import com.sparta.final_project.domain.bid.dto.response.BidResponse;
import com.sparta.final_project.domain.bid.dto.response.BidSimpleResponse;
import com.sparta.final_project.domain.bid.entity.Bid;
import com.sparta.final_project.domain.bid.repository.BidRepository;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BidService {
    private final BidRepository bidRepository;
    private final UserRepository userRepository;
    private final AuctionRepository auctionRepository;

    @Transactional
    public BidResponse createBid(Long userId, BidRequest request) {
        User user = userRepository.findById(userId).orElseThrow(()-> new NullPointerException("해당하는 아이디의 유저가 존재하지 않습니다"));
        Auction auction = auctionRepository.findById(request.getAuctionId()).orElseThrow(() -> new NullPointerException("해당하는 경매가 존재하지 않습니다."));
        List<Bid> bidList = bidRepository.findAllByAuctionOrderByCreatedAtDesc(auction);
        int maxBid = bidList.size()==0? auction.getStartPrice()-1 : bidList.get(0).getPrice();
        if(request.getPrice()<=maxBid) throw new OhapjijoleException(ErrorCode._NOT_LARGER_PRICE);
        Bid bid = new Bid(request, user, auction);
        Bid newBid = bidRepository.save(bid);
        return new BidResponse(newBid);
    }

    public List<BidSimpleResponse> getBids(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new NullPointerException("해당하는 경매가 존재하지 않습니다."));
        List<Bid> bidList = bidRepository.findAllByAuctionOrderByCreatedAtDesc(auction);
        return bidList.stream().map(BidSimpleResponse::new).toList();
    }
}
