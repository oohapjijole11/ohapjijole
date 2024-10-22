package com.sparta.final_project.domain.auction.service;

import com.sparta.final_project.domain.auction.dto.request.AuctionRequest;
import com.sparta.final_project.domain.auction.dto.response.AuctionResponse;
import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;
//    private final UserRepository userRepository;


//    생성
    public AuctionResponse createAuction(AuctionRequest auctionRequest) {
//        경매 등급 측정, 경매 상태, AuctionEntity 정보 저장 메소드
        Auction auction = gradeMeasurement(auctionRequest);
        auctionRepository.save(auction);
        return new AuctionResponse(auction);
    }
//    단건조회
    public AuctionResponse getAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 경매입니다."));

        return new AuctionResponse(auction);
    }
//    다건조회
    public List<AuctionResponse> getAuctionList() {
        List<Auction> auctionList = auctionRepository.findAll();
        return auctionList.stream().map(AuctionResponse::new).toList();
    }
//    수정
    public AuctionResponse updateAuction(Long auctionId, AuctionRequest auctionRequest) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 경매입니다."));
        auction.update(auctionRequest);
        gradeMeasurement(auctionRequest);
        auctionRepository.save(auction);
        return new AuctionResponse(auction);
    }
//    삭제
    public void deleteAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 경매입니다."));
        auctionRepository.delete(auction);
    }

    public Auction gradeMeasurement(AuctionRequest auctionRequest){
        Auction auction = new Auction(auctionRequest);
        if(auctionRequest.getStartPrice() <= 10000000){
            auction.setGrade(Grade.C);
        } else if(auctionRequest.getStartPrice() <= 15000000) {
            auction.setGrade(Grade.B);
        } else if(auctionRequest.getStartPrice() <= 20000000) {
            auction.setGrade(Grade.A);
        } else if(auctionRequest.getStartPrice() <= 30000000) {
            auction.setGrade(Grade.S);
        } else {
            auction.setGrade(Grade.L);
        }
        auction.setStatus(Status.BID);
        return auction;
    }

//    유저,경매 검증
//    public Auction validateAuction(Authen authen, AuctionResponse AuctionResponse){
//        User loggedInUser = userRepository.findById(authen.getId()).orElseThrow(()
//                -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
//        Auction auction = auctionRepository.findById(AuctionResponse.getAuctionId()).orElseThrow(()
//                -> new IllegalArgumentException("존재하지 않는 경매입니다."));
//        return auction;
//    }

}
