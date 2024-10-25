package com.sparta.final_project.domain.auction.service;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.auction.dto.request.AuctionRequest;
import com.sparta.final_project.domain.auction.dto.response.AuctionResponse;
import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.item.repository.ItemRepository;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private long remainingSeconds;

//    경매시작
    @Scheduled(fixedRate = 1000)
    public void startAuction() {
        List<Auction> auctions = auctionRepository.findAll();
        for (Auction auction : auctions) {
            LocalDateTime now = LocalDateTime.now();
            if(auction.getStatus().equals(Status.WAITING) && now.isAfter(auction.getStartTime())) {
                auction.setStatus(Status.BID);
                auctionRepository.save(auction);
            }
            if(auction.getStatus().equals(Status.BID) && now.isAfter(auction.getEndTime())) {
                auction.setStatus(Status.SUCCESSBID);
                auctionRepository.save(auction);
            }
        }
    }

    public long getRemainingTime(Auction auction) {
        if(auction.getStatus().equals(Status.BID) && auction.getEndTime() != null) {
            remainingSeconds = Duration.between(LocalDateTime.now(), auction.getEndTime()).getSeconds();
            if(remainingSeconds <= 0){
                remainingSeconds = 0;
            }
        }
        return remainingSeconds;
    }




//    생성
    public AuctionResponse createAuction(AuthUser authUser, Long itemId, AuctionRequest auctionRequest) {
        userRepository.findById(authUser.getId()).orElseThrow(() -> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        itemRepository.findById(itemId).orElseThrow(()-> new OhapjijoleException(ErrorCode._NOT_FOUND_ITEM));
//        경매 등급 측정, 경매 상태, AuctionEntity 정보 저장 메소드
        Auction auction = gradeMeasurement(auctionRequest);
        auctionRepository.save(auction);
        return new AuctionResponse(auction);
    }
//    단건조회
    public AuctionResponse getAuction(AuthUser authUser,Long auctionId) {
        userRepository.findById(authUser.getId()).orElseThrow(() -> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_AUCTION));
        return new AuctionResponse(auction);
    }
//    다건조회
    public List<AuctionResponse> getAuctionList(AuthUser authUser) {
        userRepository.findById(authUser.getId()).orElseThrow(()-> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        List<Auction> auctionList = auctionRepository.findAll();
        return auctionList.stream().map(AuctionResponse::new).toList();
    }
//    수정
    public AuctionResponse updateAuction(AuthUser authUser,Long auctionId, AuctionRequest auctionRequest) {
        userRepository.findById(authUser.getId()).orElseThrow(()-> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_AUCTION));
        if(auction.getStatus() != Status.WAITING){
            throw new OhapjijoleException(ErrorCode._NOT_ALLOWED_TO_UPDATE);
        }
//        수정메소드
        gradeMeasurementForUpdate(auction,auctionRequest);
        auctionRepository.save(auction);
        return new AuctionResponse(auction);
    }
//    삭제
    public void deleteAuction(AuthUser authUser,Long auctionId) {
        userRepository.findById(authUser.getId()).orElseThrow(()-> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_AUCTION));
        auctionRepository.delete(auction);
    }

//    경매 시작
    @Scheduled(fixedRate = 1000)
    public void startAuctionScheduler() {
        LocalDateTime now = LocalDateTime.now();
        List<Auction> auctions = auctionRepository.findAllByStatusAndStartTimeLessThan(Status.WAITING, now);
        for (Auction auction : auctions) {
            auction.setStatus(Status.BID);
            auctionRepository.save(auction);
        }
    }

    //    경매 마감
    @Scheduled(fixedRate = 1000)
    public void endAuctionScheduler() {
        LocalDateTime now = LocalDateTime.now();
        List<Auction> auctions = auctionRepository.findAllByStatusAndEndTimeLessThan(Status.BID, now);
        for (Auction auction : auctions) {
            auction.setStatus(Status.SUCCESSBID);
//            유찰 추가
            auctionRepository.save(auction);
        }
    }




//    경매 생성 및 등급 측정
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
        auction.setStatus(Status.WAITING);
        return auction;
    }

//    경매 수정
    public void gradeMeasurementForUpdate(Auction auction, AuctionRequest auctionRequest){
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
        auction.setStatus(Status.WAITING);
    }
}
