package com.sparta.final_project.domain.bid.service;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.bid.dto.request.BidRequest;
import com.sparta.final_project.domain.bid.dto.response.BidResponse;
import com.sparta.final_project.domain.bid.entity.Bid;
import com.sparta.final_project.domain.bid.repository.BidRepository;
import com.sparta.final_project.domain.bid.repository.EmitterRepository;
import com.sparta.final_project.domain.bid.repository.RedisRepository;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.ticket.entity.BuyTickets;
import com.sparta.final_project.domain.ticket.entity.Ticket;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.entity.UserRole;
import com.sparta.final_project.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidServiceTest {
    @Mock
    EmitterRepository emitterRepository;

    @Mock
    BidRepository bidRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    AuctionRepository auctionRepository;

    @Mock
    BidCommonService commonService;

    @InjectMocks
    BidService bidService;

    @Mock
    RedisRepository redisRepository;

    private User user;
    private Auction auction;
    private Bid firstBid;
    private Bid lastBid;
    private BuyTickets buyTickets;
    private Ticket ticket;
    SseEmitter emitter= new SseEmitter();
    HashMap<String, Object> events = new HashMap<>();
    private AuthUser authUser;

    private BidRequest request;


    @BeforeEach
    void setUp() {
        authUser = new AuthUser(1L, "test","email", UserRole.USER);
        user = new User();
        ReflectionTestUtils.setField(user, "id", 1L);

        auction = new Auction();
        ReflectionTestUtils.setField(auction, "id", 1L);
        ReflectionTestUtils.setField(auction, "startPrice", 50000);
        ReflectionTestUtils.setField(auction, "status", Status.BID);

        firstBid = new Bid();
        ReflectionTestUtils.setField(firstBid, "id", 1L);
        ReflectionTestUtils.setField(firstBid, "price", 50000);
        ReflectionTestUtils.setField(firstBid,"auction",auction);
        ReflectionTestUtils.setField(firstBid,"user",user);
        lastBid = new Bid();
        ReflectionTestUtils.setField(lastBid, "id", 2L);
        ReflectionTestUtils.setField(lastBid, "price", 60000);
        ReflectionTestUtils.setField(lastBid,"auction",auction);
        ReflectionTestUtils.setField(lastBid,"user",user);

        request = new BidRequest();
        ReflectionTestUtils.setField(request, "price", 60000);

        ticket = new Ticket();
        ReflectionTestUtils.setField(ticket, "id", 1L);
        ReflectionTestUtils.setField(ticket, "auction", auction);

        buyTickets = new BuyTickets();
        ReflectionTestUtils.setField(buyTickets, "id", 1L);
        ReflectionTestUtils.setField(buyTickets, "user", user);
        ReflectionTestUtils.setField(buyTickets, "ticket", ticket);
        ReflectionTestUtils.setField(user, "tickets", List.of(buyTickets));
    }

    @Test
    void subscribe_success() {
        //given
        Long auctionId = 1L;
        String eventId = "1_56644566";
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
        given(auctionRepository.findById(anyLong())).willReturn(Optional.ofNullable(auction));
        given(commonService.makeTimeIncludeId(auctionId)).willReturn(eventId);
        given(emitterRepository.save(any(String.class),any(SseEmitter.class))).willReturn(emitter);
        doNothing().when(commonService).sendToClient(any(), any(), any(), any(), any());
        given(redisRepository.findAllEventWithAuctionId(any(String.class))).willReturn(events);
//
//        //when
        SseEmitter result = bidService.subscribe(authUser, auctionId, "");
//        //then
        assertNotNull(result);
        verify(commonService, atLeastOnce()).sendToClient(any(), any(), any(), any(), any());
    }

    @Test
    void subscribe_경매_시작_20분_이전() {
        //given
        Long auctionId = 1L;
        ReflectionTestUtils.setField(auction, "status", Status.WAITING);
        ReflectionTestUtils.setField(auction, "startTime", LocalDateTime.now().plusMinutes(30L));

        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
        given(auctionRepository.findById(anyLong())).willReturn(Optional.ofNullable(auction));

        //when
        OhapjijoleException exception = assertThrows(OhapjijoleException.class, ()-> bidService.subscribe(authUser, auctionId, ""));

        //then
        assertEquals("아직 시작하지 않은 경매입니다.", exception.getMessage());
    }

    @Test
    void subscribe_경매_낙찰() {
        //given
        Long auctionId = 1L;
        ReflectionTestUtils.setField(auction, "status", Status.SUCCESSBID);

        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
        given(auctionRepository.findById(anyLong())).willReturn(Optional.ofNullable(auction));

        //when
        OhapjijoleException exception = assertThrows(OhapjijoleException.class, ()-> bidService.subscribe(authUser, auctionId, ""));

        //then
        assertEquals("이미 끝난 경매입니다.", exception.getMessage());
    }

    @Test
    void subscribe_경매_유찰() {
        //given
        Long auctionId = 1L;
        ReflectionTestUtils.setField(auction, "status", Status.FAILBID);
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
        given(auctionRepository.findById(anyLong())).willReturn(Optional.ofNullable(auction));

        //when
        OhapjijoleException exception = assertThrows(OhapjijoleException.class, ()-> bidService.subscribe(authUser, auctionId, ""));

        //then
        assertEquals("이미 끝난 경매입니다.", exception.getMessage());
    }

    @Test
    void subscribe_경매_시작전_입장() {
        //given
        Long auctionId = 1L;
        String eventId = "1_56644566";
        ReflectionTestUtils.setField(auction, "status", Status.WAITING);
        ReflectionTestUtils.setField(auction, "startTime", LocalDateTime.now().plusMinutes(10L));
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
        given(auctionRepository.findById(anyLong())).willReturn(Optional.ofNullable(auction));
        given(commonService.makeTimeIncludeId(auctionId)).willReturn(eventId);
        given(emitterRepository.save(any(String.class),any(SseEmitter.class))).willReturn(emitter);
        doNothing().when(commonService).sendToClient(any(), any(), any(), any(), any());
        given(redisRepository.findAllEventWithAuctionId(any(String.class))).willReturn(events);
//
//        //when
        SseEmitter result = bidService.subscribe(authUser, auctionId, "");
//        //then
        assertNotNull(result);
        verify(commonService, atLeast(3)).sendToClient(any(), any(), any(), any(), any());
    }

    @Test
    void subscribe_티켓_없음(){
        //given
        Long auctionId = 1L;
        ReflectionTestUtils.setField(user, "tickets", new ArrayList<>());
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
        given(auctionRepository.findById(anyLong())).willReturn(Optional.ofNullable(auction));
        //when
        OhapjijoleException exception = assertThrows(OhapjijoleException.class, ()-> bidService.subscribe(authUser, auctionId, ""));
        //then
        assertEquals("경매 티켓을 가지고 있지 않습니다.", exception.getMessage());
    }

    @Test
    void createBid_success(){
        //given
        Long auctionId = 1L;
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
        given(auctionRepository.findById(anyLong())).willReturn(Optional.ofNullable(auction));
        given(bidRepository.findAllByAuctionOrderByCreatedAtDesc(auction)).willReturn(List.of(firstBid));
        given(bidRepository.save(any(Bid.class))).willReturn(lastBid);
        doNothing().when(commonService).sseSend(any(Bid.class), eq(Status.BID));

        //when
        BidResponse result = bidService.createBid(1L, auctionId, request);
        //then
        assertNotNull(result);
        assertEquals(request.getPrice(), result.getPrice());

    }

    @Test
    void createBid_경매_중이_아닐때() {
        //given
        Long auctionId = 1L;
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
        given(auctionRepository.findById(anyLong())).willReturn(Optional.ofNullable(auction));
        ReflectionTestUtils.setField(auction, "status", Status.SUCCESSBID);
        //when
        OhapjijoleException exception = assertThrows(OhapjijoleException.class, ()-> bidService.createBid(1L, auctionId, request));
        //then
        assertEquals("경매가 진행되고 있지 않습니다.", exception.getMessage());
    }

    @Test
    void createBid_처음_입찰일때() {
        //given
        Long auctionId = 1L;
        ReflectionTestUtils.setField(request, "price", 50000);
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
        given(auctionRepository.findById(anyLong())).willReturn(Optional.ofNullable(auction));
        given(bidRepository.findAllByAuctionOrderByCreatedAtDesc(auction)).willReturn(new ArrayList<>());
        given(bidRepository.save(any(Bid.class))).willReturn(firstBid);
        doNothing().when(commonService).sseSend(any(Bid.class), eq(Status.BID));
        //when
        BidResponse result = bidService.createBid(1L, auctionId, request);
        //then
        assertEquals(request.getPrice(), result.getPrice());
    }

    @Test
    void createBid_금액이_최고금액보다_낮을떄() {
        //given
        Long auctionId = 1L;
        ReflectionTestUtils.setField(request, "price", 50000);
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(user));
        given(auctionRepository.findById(anyLong())).willReturn(Optional.ofNullable(auction));
        given(bidRepository.findAllByAuctionOrderByCreatedAtDesc(auction)).willReturn(List.of(firstBid));
        //when
        OhapjijoleException exception = assertThrows(OhapjijoleException.class, ()-> bidService.createBid(1L, auctionId, request));
        //then
        assertEquals("입찰가는 현재 최고 입찰가보다 커야합니다.", exception.getMessage());
    }





}