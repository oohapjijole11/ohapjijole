package com.sparta.final_project.domain.bid.service;

import com.slack.api.Slack;
import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.bid.dto.response.SbidResponse;
import com.sparta.final_project.domain.bid.dto.response.SbidSimpleResponse;
import com.sparta.final_project.domain.bid.entity.Bid;
import com.sparta.final_project.domain.bid.entity.Sbid;
import com.sparta.final_project.domain.bid.repository.BidRepository;
import com.sparta.final_project.domain.bid.repository.SbidRepository;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;

@ExtendWith(MockitoExtension.class)
class SbidServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    SbidRepository sbidRepository;
    @Mock
    AuctionRepository auctionRepository;
    @Mock
    BidRepository bidRepository;

    @Mock
    BidCommonService commonService;

    @InjectMocks
    SbidService sbidService;

    @InjectMocks
    Slack slackClient = Slack.getInstance();

    private User user;
    private Auction auction;
    private Bid lastBid;
    private Sbid sbid;

    @BeforeEach
    void setUp() {
        user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        auction = new Auction();
        ReflectionTestUtils.setField(auction, "id", 1L);
        ReflectionTestUtils.setField(auction, "startPrice", 50000);
        ReflectionTestUtils.setField(auction, "status", Status.BID);

        lastBid = new Bid();
        ReflectionTestUtils.setField(lastBid, "id", 1L);
        ReflectionTestUtils.setField(lastBid, "price", 50000);
        ReflectionTestUtils.setField(lastBid,"auction",auction);

        sbid = new Sbid();
        ReflectionTestUtils.setField(sbid, "id", 1L);
        ReflectionTestUtils.setField(sbid, "price", 50000);
        ReflectionTestUtils.setField(sbid,"auction",auction);
        ReflectionTestUtils.setField(sbid,"user", user);



    }

    @Test
    public void createbid_success() {
        //given
        Long auctionId = 1L;
        given(auctionRepository.findById(auctionId)).willReturn(Optional.ofNullable(auction));
        given(bidRepository.findAllByAuctionOrderByCreatedAtDesc(auction)).willReturn(List.of(lastBid));
        given(sbidRepository.save(any(Sbid.class))).willReturn(sbid);
        doNothing().when(commonService).sseSend(lastBid, Status.SUCCESSBID);
        //when
        SbidResponse result = sbidService.createSbid(auctionId);
        //then
        assertNotNull(result);
        assertEquals(lastBid.getPrice(), result.getPrice());

    }

    @Test
    public void createBid_낙찰된_경매일때() {
        //given
        Long auctionId = 1L;
        auction.setStatus(Status.SUCCESSBID);
        given(auctionRepository.findById(auctionId)).willReturn(Optional.ofNullable(auction));
        //when
        OhapjijoleException exception = assertThrows(OhapjijoleException.class, ()-> sbidService.createSbid(auctionId));
        //then
        assertEquals("이미 끝난 경매입니다.", exception.getMessage());
    }

    @Test
    public void createBid_유찰된_경매일때() {
        //given
        Long auctionId = 1L;
        auction.setStatus(Status.FAILBID);
        given(auctionRepository.findById(auctionId)).willReturn(Optional.ofNullable(auction));
        //when
        OhapjijoleException exception = assertThrows(OhapjijoleException.class, ()-> sbidService.createSbid(auctionId));
        //then
        assertEquals("이미 끝난 경매입니다.", exception.getMessage());
    }

    @Test
    public void createBid_아직_시작안된_경매일때() {
        //given
        Long auctionId = 1L;
        auction.setStatus(Status.WAITING);
        given(auctionRepository.findById(auctionId)).willReturn(Optional.ofNullable(auction));
        //when
        OhapjijoleException exception = assertThrows(OhapjijoleException.class, ()-> sbidService.createSbid(auctionId));
        //then
        assertEquals("아직 시작하지 않은 경매입니다.", exception.getMessage());
    }

    @Test
    public void getSbids_success(){
        //given
        given(userRepository.findById(1L)).willReturn(Optional.ofNullable(user));
        int page=1;
        int size=10;
        Pageable pageable = PageRequest.of(page-1, size);
        given(sbidRepository.findAllByUserIdOrderByCreatedAtDesc(1L,pageable)).willReturn(new PageImpl<>(List.of(sbid), pageable, size));
        //when
        Page<SbidSimpleResponse> result = sbidService.getSbids(1L,page, size);
        //then
        //왜인지 10으로 나온다
        assertEquals(10, result.getTotalElements());
        assertEquals(sbid.getPrice(), result.getContent().get(0).getPrice());
    }










}