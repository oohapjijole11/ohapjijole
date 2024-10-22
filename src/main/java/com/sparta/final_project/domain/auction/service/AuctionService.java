package com.sparta.final_project.domain.auction.service;

import com.sparta.final_project.domain.auction.dto.AuctionRequestDto;
import com.sparta.final_project.domain.auction.dto.AuctionResponseDto;
import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Grade;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuctionService {

    private final AuctionRepository auctionRepository;


//    생성
    public AuctionResponseDto createAuction(AuctionRequestDto auctionRequestDto) {
//        경매 등급 측정, 경매 상태, AuctionEntity 정보 저장 메소드
        Auction auction = gradeMeasurement(auctionRequestDto);
        auctionRepository.save(auction);
        return new AuctionResponseDto(auction);
    }
//    단건조회
    public AuctionResponseDto getAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 경매입니다."));

        return new AuctionResponseDto(auction);
    }
//    다건조회
    public List<AuctionResponseDto> getAuctionList() {
        List<Auction> auctionList = auctionRepository.findAll();
        return auctionList.stream().map(AuctionResponseDto::new).toList();
    }
//    수정
    public AuctionResponseDto updateAuction(Long auctionId, AuctionRequestDto auctionRequestDto) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 경매입니다."));
        auction.update(auctionRequestDto);
        auctionRepository.save(auction);
        return new AuctionResponseDto(auction);
    }
//    삭제
    public void deleteAuction(Long auctionId) {
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(()
                -> new IllegalArgumentException("존재하지 않는 경매입니다."));
        auctionRepository.delete(auction);
    }

    public Auction gradeMeasurement(AuctionRequestDto auctionRequestDto){
        Auction auction = new Auction(auctionRequestDto);
        if(auctionRequestDto.getStartPrice() <= 10000000){
            auction.setGrade(Grade.C);
        } else if(auctionRequestDto.getStartPrice() <= 15000000) {
            auction.setGrade(Grade.B);
        } else if(auctionRequestDto.getStartPrice() <= 20000000) {
            auction.setGrade(Grade.A);
        } else if(auctionRequestDto.getStartPrice() <= 30000000) {
            auction.setGrade(Grade.S);
        } else {
            auction.setGrade(Grade.L);
        }
        auction.setStatus(Status.BID);
        return auction;
    }

}
