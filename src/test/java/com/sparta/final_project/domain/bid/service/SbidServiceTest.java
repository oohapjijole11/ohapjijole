package com.sparta.final_project.domain.bid.service;

import com.slack.api.Slack;
import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.bid.dto.response.SbidResponse;
import com.sparta.final_project.domain.bid.entity.Bid;
import com.sparta.final_project.domain.bid.entity.Sbid;
import com.sparta.final_project.domain.bid.repository.BidRepository;
import com.sparta.final_project.domain.bid.repository.SbidRepository;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
//        willDoNothing().given(commonService.sseSend(lastBid, Status.SUCCESSBID))
//        given(commonService.sseSend(lastBid, Status.SUCCESSBID)).will
        doNothing().when(commonService).sseSend(lastBid, Status.SUCCESSBID);
        //when
        SbidResponse result = sbidService.createSbid(auctionId);
        //then
        assertNotNull(result);
        assertEquals(lastBid.getPrice(), result.getPrice());

    }






}