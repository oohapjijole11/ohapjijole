package com.sparta.final_project.domain.ticket.service;

import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import com.sparta.final_project.domain.ticket.entity.Ticket;
import com.sparta.final_project.domain.ticket.repository.TicketRepository;
import com.sparta.final_project.domain.ticket.service.TicketBuyService;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sparta.final_project.domain.ticket.entity.TicketStatus.PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;

// 통합 테스트 코드
@SpringBootTest
@Execution(ExecutionMode.CONCURRENT)
@DisplayName("분산 락")
public class TicketBuyServiceTest {

    @Autowired
    private TicketBuyService ticketBuyService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    private Ticket ticket;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        userRepository.save(user);

        ticket = new Ticket();
        ticket.setTicketTitle("Ticket Title");
        ticket.setTicketDescription("Ticket Description");
        ticket.setTicketStatus(PROGRESS);
        ticket.setTicketCount(100L);  // 초기 티켓 수 설정
        ticketRepository.save(ticket);
    }

    @Test
    public void ticketBuyServiceTest() throws InterruptedException {
        int threadCount = 1000;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // 성공과 실패 횟수를 기록하기 위한 카운터
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        // 테스트 시작 시간 기록
        long startTime = System.currentTimeMillis();

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    BuyTicketsRequest request = new BuyTicketsRequest(ticket.getId(), user.getId());
                    String result = ticketBuyService.buyTicket(request);

                    // 성공 여부에 따라 카운터 증가
                    if ("티켓 구매가 완료되었습니다.".equals(result)) {
                        successCount.incrementAndGet();
                    } else {
                        failureCount.incrementAndGet();
                    }
                } catch (Exception e) {
                    // 예외 발생 시 실패로 처리
                    failureCount.incrementAndGet();
                }
                finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // 테스트 종료 시간 기록
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        Ticket updatedTicket = ticketRepository.findById(ticket.getId()).orElseThrow();

        // 검증: 성공한 구매 수는 최대 100, 실패한 구매 수는 900이 되어야 함
        assertThat(successCount.get()).isEqualTo(100);
        assertThat(failureCount.get()).isEqualTo(900);

        // 남은 티켓 수 확인
        assertThat(updatedTicket.getTicketCount()).isEqualTo(0);

        // 테스트 소요 시간과 성공한 구매 수 콘솔 출력
        System.out.println("테스트 소요 시간: " + totalTime + " ms");
        System.out.println("성공적으로 구매된 티켓 수: " + successCount.get());
        System.out.println("실패한 구매 요청 수: " + failureCount.get());
    }
}
