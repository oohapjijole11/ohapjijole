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
import com.sparta.final_project.domain.item.entity.Item;
import com.sparta.final_project.domain.item.repository.ItemRepository;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuctionService {

    private final AuctionRepository auctionRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    //    생성
    public AuctionResponse createAuction(AuthUser authUser, Long itemId, AuctionRequest auctionRequest) {
        userRepository.findById(authUser.getId()).orElseThrow(() -> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_ITEM));
//        상품이 유찰일때 다시 등록
        Auction auction = auctionRepository.findByItemIdAndStatusIn(item.getId(), List.of(Status.WAITING, Status.BID, Status.SUCCESSBID));
        if (auction != null) {
            throw new OhapjijoleException(ErrorCode._NOT_FAILED_AUCTION);
        }
        if (auctionRequest.getStartTime().isBefore(auctionRequest.getEndTime()) || auctionRequest.getStartTime().isEqual(auctionRequest.getEndTime())) {
            throw new OhapjijoleException(ErrorCode._BAD_REQUEST_TIME);
        }
//        경매 등급 측정, 경매 상태, AuctionEntity 정보 저장 메소드
        auction = gradeMeasurement(auctionRequest);
        auction.setItem(item);
        auctionRepository.save(auction);
//        Redis 저장
        return new AuctionResponse(auction);
    }

    //    단건조회
    public AuctionResponse getAuction(AuthUser authUser, Long auctionId) {
        userRepository.findById(authUser.getId()).orElseThrow(() -> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_AUCTION));
        return new AuctionResponse(auction);
    }

    //    다건조회
    public List<AuctionResponse> getAuctionList(AuthUser authUser) {
        userRepository.findById(authUser.getId()).orElseThrow(() -> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        List<Auction> auctionList = auctionRepository.findAll();
        return auctionList.stream().map(AuctionResponse::new).toList();
    }

    //    수정
    public AuctionResponse updateAuction(AuthUser authUser, Long auctionId, AuctionRequest auctionRequest) {
        userRepository.findById(authUser.getId()).orElseThrow(() -> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_AUCTION));
        if (auction.getStatus() != Status.WAITING) {
            throw new OhapjijoleException(ErrorCode._NOT_ALLOWED_TO_UPDATE);
        }
//        수정메소드
        gradeMeasurementForUpdate(auction, auctionRequest);
        auctionRepository.save(auction);
        return new AuctionResponse(auction);
    }

    //    삭제
    public void deleteAuction(AuthUser authUser, Long auctionId) {
        userRepository.findById(authUser.getId()).orElseThrow(() -> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));
        Auction auction = auctionRepository.findById(auctionId).orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_AUCTION));
        auctionRepository.delete(auction);
    }

    //    경매 생성 및 등급 측정
    public Auction gradeMeasurement(AuctionRequest auctionRequest) {
        Auction auction = new Auction(auctionRequest);
        if (auctionRequest.getStartPrice() <= 10000000) {
            auction.setGrade(Grade.C);
        } else if (auctionRequest.getStartPrice() <= 15000000) {
            auction.setGrade(Grade.B);
        } else if (auctionRequest.getStartPrice() <= 20000000) {
            auction.setGrade(Grade.A);
        } else if (auctionRequest.getStartPrice() <= 30000000) {
            auction.setGrade(Grade.S);
        } else {
            auction.setGrade(Grade.L);
        }
        auction.setStatus(Status.WAITING);
        return auction;
    }

    //    경매 수정
    public void gradeMeasurementForUpdate(Auction auction, AuctionRequest auctionRequest) {
        if (auctionRequest.getStartPrice() <= 10000000) {
            auction.setGrade(Grade.C);
        } else if (auctionRequest.getStartPrice() <= 15000000) {
            auction.setGrade(Grade.B);
        } else if (auctionRequest.getStartPrice() <= 20000000) {
            auction.setGrade(Grade.A);
        } else if (auctionRequest.getStartPrice() <= 30000000) {
            auction.setGrade(Grade.S);
        } else {
            auction.setGrade(Grade.L);
        }
        auction.setStatus(Status.WAITING);
    }
}
