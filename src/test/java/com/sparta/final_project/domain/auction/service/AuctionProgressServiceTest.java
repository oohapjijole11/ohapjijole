package com.sparta.final_project.domain.auction.service;

import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.auction.entity.Status;
import com.sparta.final_project.domain.auction.repository.AuctionRepository;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.entity.UserRole;
import com.sparta.final_project.domain.user.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@ExtendWith(MockitoExtension.class)
//@MockitoSettings(strictness = Strictness.LENIENT)
public class AuctionProgressServiceTest {

    @Mock
    private RedissonClient redissonClient;

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RLock lock;

    @InjectMocks
    private AuctionProgressService auctionProgressService;

    private ExecutorService executor;

    @BeforeEach
    void setUp() {
        executor = Executors.newSingleThreadExecutor();

        assertNotNull(auctionProgressService, "AuctionProgressService");
        assertNotNull(userRepository, "userRepository주입 실패");
        assertNotNull(auctionRepository, "auctionRepository 주입 실패")
;
    }

    @Test
    /**
     * 목적 -> 경매가 진중인 경우(Status.BID) 경매 종료 시간을 모니터링하며, 종료 시간이 되었을 때,
     * 경매 상태를 Status.SUCCESSBID로 변경하는 startAuctionCountdown메서드를 테스트함
     */
    public void testStartAuctionCountdown() throws Exception {
        // given
        Long auctionId = 1L;
        Long userId = 2L;
        AuthUser authUser = new AuthUser(userId, "username", "user@naver.com", UserRole.USER);
        Auction auction = new Auction();
        auction.setStatus(Status.BID);
        auction.setEndTime(LocalDateTime.now().plusMinutes(2));  // 끝나는 시간 설정
//        auction.setStatus(Status.WAITING);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
        when(auctionRepository.findByIdAndStatus(auctionId, Status.BID)).thenReturn(auction);
        when(redissonClient.getLock("auction-end-lock: " + auctionId)).thenReturn(lock);
        when(lock.tryLock()).thenReturn(true);

        CountDownLatch latch = new CountDownLatch(1);

        // Latch를 줄이는 동작을 unLock에서 수행하여 대기 해제
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(lock).unlock();

        // when
        SseEmitter sseEmitter = auctionProgressService.startAuctionCountdown(authUser, auctionId);

        // Latch 대기 -> 비동기 작업이 완료될 때까지 대기
        latch.await();

        // then
        assertNotNull(sseEmitter);
        verify(auctionRepository, times(1)).findByIdAndStatus(auctionId, Status.BID);
        verify(lock, times(1)).tryLock();

        // 추가 확인: 경매가 진행 중 상태로 되었는지 확인
        assertEquals(Status.IN_PROGRESS, auction.getStatus());
        verify(auctionRepository, times(1)).save(auction);
    }

    /**
     * 경매 시작 시간이 되었을 때 Status.WAITING 상태의 경매가 Status.BID로 변경되는지
     * 확인하기 위해
     */
    @Test
    public void testMonitorAuctionStart() throws Exception {
        // given
        Long auctionId = 1L;
        Long userId = 2L;
        AuthUser authUser = new AuthUser(userId, "username", "user@naver.com", UserRole.USER);
        Auction auction = new Auction();
        auction.setStartTime(LocalDateTime.now().minusMinutes(1));  // 이미 시작한 시간
//        auction.setStatus(Status.WAITING);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));
        when(redissonClient.getLock("auction-end-lock: " + auctionId)).thenReturn(lock);
        when(lock.tryLock()).thenReturn(true);

        CountDownLatch latch = new CountDownLatch(1);

        // 비동기 작업 완료 시 latch.countDown() 호출
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(lock).unlock();

        // when
        SseEmitter sseEmitter = auctionProgressService.monitorAuctionStart(authUser, auctionId);

        // Latch 대기( 비동기 작업 완료까지 대기)
        latch.await();

        // then
        assertNotNull(sseEmitter);
        verify(auctionRepository, times(1)).findById(auctionId);
        verify(lock, times(1)).tryLock();
        verify(auctionRepository, times(1)).save(auction);
        assertEquals(Status.BID, auction.getStatus());
    }

    /**
     * startAuctionCountdown 메서드 호출 시 인증 된 사용자가 없을 경우 예외가 발생하는지
     * 확인
     */
    @Test
    public void testStartAuctionCountdownWithUserNotFound() {
        // given
        Long auctionId = 1L;
        Long userId = 2L;
        AuthUser authUser = new AuthUser(userId, "username", "user@naver.com", UserRole.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when, then
        OhapjijoleException exception = assertThrows(OhapjijoleException.class, () ->
                auctionProgressService.startAuctionCountdown(authUser, auctionId)
        );

        assertEquals(ErrorCode._USER_NOT_FOUND, exception.getErrorCode());
    }

    /**
     * monitorAuctionStart 메서드 호출 시 지정된 경매가 존재하지 않는 경우 예외가 발생하는지 확인
     */
    @Test
    public void testMonitorAuctionStartWithAuctionNotFound() {
        // given
        Long auctionId = 1L;
        Long userId = 2L;
        AuthUser authUser = new AuthUser(userId, "username", "user@naver.com", UserRole.USER);

        when(userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.empty());

        // when, then
        OhapjijoleException exception = assertThrows(OhapjijoleException.class, () ->
                auctionProgressService.monitorAuctionStart(authUser, auctionId)
        );

        assertEquals(ErrorCode._NOT_FOUND_AUCTION, exception.getErrorCode());
    }

    @Test
    public void testStartAuctionCountdownInProgress() throws Exception {
        // given
        Long auctionId = 1L;
        Long userId = 2L;

        AuthUser authUser = new AuthUser(userId, "username", "user@naver.com", UserRole.USER);
        Auction auction = new Auction();
        auction.setStatus(Status.BID);
        auction.setEndTime(LocalDateTime.now().plusMinutes(1)); // 끝나는 시간 설정

        when(userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
        when(auctionRepository.findByIdAndStatus(auctionId, Status.BID)).thenReturn(auction);
        when(redissonClient.getLock("auction-end-lock: " + auctionId)).thenReturn(lock);
        when(lock.tryLock()).thenReturn(true);

        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(lock).unlock();

        // when
        SseEmitter sseEmitter = auctionProgressService.startAuctionCountdown(authUser, auctionId);

        // Latch 대기 -> 비동기 작업이 완료될 때 까지 대기
        latch.await();

        // then
        assertNotNull(sseEmitter);
        verify(auctionRepository, times(1)).findByIdAndStatus(auctionId, Status.BID);
        verify(lock, times(1)).tryLock();

        // 추가 확인: 경매가 진행 중 상태로 업데이트되었는지 확인
        assertEquals(Status.IN_PROGRESS, auction.getStatus());
        verify(auctionRepository, times(1)).save(auction);
    }

    @Test
    public void testStartAuctionCountdownEndsWithSuccessBid() throws Exception {
        // given
        Long auctionId = 1L;
        Long userId = 2L;
        AuthUser authUser = new AuthUser(userId, "username", "user@naver.com", UserRole.USER);
        Auction auction = new Auction();
        auction.setStatus(Status.BID);
        auction.setEndTime(LocalDateTime.now().plusSeconds(1)); // 빠르게 종료 시간 설정

        when(userRepository.findById(userId)).thenReturn(Optional.of(mock(User.class)));
        when(auctionRepository.findByIdAndStatus(auctionId, Status.BID)).thenReturn(auction);
        when(redissonClient.getLock("auction-end-lock: " + auctionId)).thenReturn(lock);
        when(lock.tryLock()).thenReturn(true);

        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(lock).unlock();

        // when
        auctionProgressService.startAuctionCountdown(authUser, auctionId);

        // latch 대기 -> 비동기 작업이 완료 될 때까지 대기
        boolean completedInTime = latch.await(2, TimeUnit.SECONDS);
        assertTrue(completedInTime, "비동기 작업이 예상 시간 내에 완료되지 않았습니다.");

        // then
        verify(auctionRepository, times(2)).save(auction); // save 호출 횟수를 2로 설정
        assertEquals(Status.SUCCESSBID, auction.getStatus());
        verify(lock, times(1)).unlock();
    }

}
// Mockito를 사용하여 의존성 주입과 메서드 호출을 검증
// 특히 monitorAuctionStart 와 startAuctionCountdown 같은 비동기 메서드의 호출 순서와 동시성 관련 로직이 예상대로 움직이는지 확인