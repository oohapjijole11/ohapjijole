package com.sparta.final_project.domain.bid.service;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.bid.entity.Bid;
import com.sparta.final_project.domain.bid.repository.EmitterRepository;
import com.sparta.final_project.domain.bid.repository.RedisRepository;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidCommonServiceTest {
    @Mock
    EmitterRepository emitterRepository;

    @Mock
    RedisRepository redisRepository;

    @Spy
    @InjectMocks
    BidCommonService bidCommonService;

    SseEmitter emitter= new SseEmitter();

    String eventId = "1_"+System.currentTimeMillis();

    private Bid lastBid;
    private Auction auction;

    @BeforeEach
    void setUp() {

        auction = new Auction();
        ReflectionTestUtils.setField(auction, "id", 1L);

        lastBid = new Bid();
        ReflectionTestUtils.setField(lastBid, "id", 1L);
        ReflectionTestUtils.setField(lastBid, "price", 50000);
        ReflectionTestUtils.setField(lastBid,"auction",auction);
    }

    @Test
    void 이벤트_아이디_만들기_달라야함() {
        String result = bidCommonService.makeTimeIncludeId(1L);
        System.out.println(result);
        assertNotEquals(1L+"_"+System.currentTimeMillis(), result);
    }

    @Test
    void sendToClient_메세지_보내기() {
        //given

        //when
        bidCommonService.sendToClient(emitter, "test",eventId, eventId, "확인");
        //then
        assertDoesNotThrow(()->emitter.send(SseEmitter.event()
                .name("test")
                .id(eventId)
                .data("확인")
                .reconnectTime(1000L)
                .build()));
    }

    @Test
    void sendToClient_메세지_보내는_서버가_강제로_중단됐을떄() {//IllegalStateException -> OhapjijoleException으로 바꿈
        //given
        emitter.complete();  //서버 종료시킴
        //when
        OhapjijoleException exception = assertThrows(OhapjijoleException.class, ()->bidCommonService.sendToClient(emitter, "test",eventId, eventId, "확인"));

        //then
        assertEquals("경매장과 연결되지 않습니다.", exception.getMessage());
    }

    @Test
    void sendToClient_메세지_보내는_서버가_시간이_되어_중단됐을떄() throws InterruptedException {//IllegalStateException -> OhapjijoleException으로 바꿈
        emitter = new SseEmitter(3L);
        Thread.sleep(30L);
        //given
        emitter.complete();  //서버 종료시킴
        //when
        OhapjijoleException exception = assertThrows(OhapjijoleException.class, ()->bidCommonService.sendToClient(emitter, "test",eventId, eventId, "확인"));

        //then
        assertEquals("경매장과 연결되지 않습니다.", exception.getMessage());
    }

    @Test
    void sseSend_입찰_알림(){
        //given
        Map<String, SseEmitter> emitters = new HashMap<>();
        emitters.put(eventId, emitter);
        given(emitterRepository.findAllEmitterStartWithByAuctionId(any(String.class))).willReturn(emitters);
//        doNothing().when(redisRepository).setbid(eventId, lastBid.getPrice());
        doNothing().when(redisRepository).setbid(any(String.class), eq(lastBid.getPrice()));

        //when
        bidCommonService.sseSend(lastBid, Status.BID);
        //then
        verify(bidCommonService, times(1)).sseSend(lastBid, Status.BID);

    }

    @Test
    void sseSend_낙찰_알림(){
        //given
        Map<String, SseEmitter> emitters = new HashMap<>();
        emitters.put(eventId, emitter);
        given(emitterRepository.findAllEmitterStartWithByAuctionId(any(String.class))).willReturn(emitters);
//        doNothing().when(redisRepository).setbid(eventId, lastBid.getPrice());
        doNothing().when(redisRepository).setbid(any(String.class), eq(lastBid.getPrice()));

        //when
        bidCommonService.sseSend(lastBid, Status.SUCCESSBID);
        //then
        verify(bidCommonService, times(1)).sseSend(lastBid, Status.SUCCESSBID);

    }

    @Test
    void sseSend_유찰_알림(){
        //given
        Map<String, SseEmitter> emitters = new HashMap<>();
        emitters.put(eventId, emitter);
        given(emitterRepository.findAllEmitterStartWithByAuctionId(any(String.class))).willReturn(emitters);
//        doNothing().when(redisRepository).setbid(eventId, lastBid.getPrice());
        doNothing().when(redisRepository).setbid(any(String.class), eq(lastBid.getPrice()));  //eventId와 lastBid.getPrice()를 직접 넣는 것보다 Strict Stubbing이 완화된다(eventId가 달라져도 괜찮음)

        //when
        bidCommonService.sseSend(lastBid, Status.FAILBID);
        //then
        verify(bidCommonService, times(1)).sseSend(lastBid, Status.FAILBID);

    }

    @Test
    void startNotification_경매시작알림() {
        //given
        Map<String, SseEmitter> emitters = new HashMap<>();
        emitters.put(eventId, emitter);
        given(emitterRepository.findAllEmitterStartWithByAuctionId(any(String.class))).willReturn(emitters);
        //when
        bidCommonService.startNotification(anyLong());
        verify(bidCommonService, times(1)).startNotification(anyLong());
        //then
    }

}