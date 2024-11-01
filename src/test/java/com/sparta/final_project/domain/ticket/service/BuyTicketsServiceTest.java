package com.sparta.final_project.domain.ticket.service;

import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import com.sparta.final_project.domain.ticket.entity.Ticket;
import com.sparta.final_project.domain.ticket.repository.TicketRepository;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static com.sparta.final_project.domain.ticket.entity.TicketStatus.PROGRESS;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("비관적 락 사용")
public class BuyTicketsServiceTest {

    @Autowired
    private TicketBuyService buyTicketsService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    private Ticket ticket;
    private User user;

    private static final Long INITIAL_TICKET_COUNT = 100L;
    private static final int TOTAL_REQUESTS = 1000;
    private AtomicInteger successfulPurchases = new AtomicInteger(0);

    private static final Logger logger = LoggerFactory.getLogger(BuyTicketsServiceTest.class);

    @BeforeEach
    public void setup() {
        // 티켓과 사용자 생성
        ticket = new Ticket();
        ticket.setTicketTitle("Ticket Title");
        ticket.setTicketDescription("Ticket Description");
        ticket.setTicketStatus(PROGRESS);
        ticket.setTicketCount(INITIAL_TICKET_COUNT);
        ticketRepository.save(ticket);
    }

    @Test
    public void testBuyTicketWithHighConcurrency() throws InterruptedException {
        long startTime = System.currentTimeMillis(); // 시작 시간 기록
        // 스레드 수 및 CountDownLatch 설정
        ExecutorService executorService = Executors.newFixedThreadPool(100); // 스레드 생성
        CountDownLatch latch = new CountDownLatch(TOTAL_REQUESTS);

        // 각 요청에 대해 새로운 USer를 생성하고 동시에 티켓 구매 시도
        for (int i = 0; i < TOTAL_REQUESTS; i++) {
            executorService.submit(() -> {
                User user = new User();
                userRepository.save(user);

                BuyTicketsRequest request = new BuyTicketsRequest(ticket.getTicketId(), user.getId(), 1L);
                try {
                    buyTicketsService.buyTicket(request);
                    successfulPurchases.incrementAndGet(); // 성공 시 카운트 증가
                } catch (Exception e) {
                    System.out.println("구매 실패: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }
        // 모든 스레드가 완료될 때 까지 대기
        latch.await();

        long endTime = System.currentTimeMillis(); // 종료 시간 기록
        long elapsedTime  = endTime - startTime;

        // 최종적으로 구매에 성공한 인원이 100명인지 확인
        assertThat(successfulPurchases.get()).isEqualTo(INITIAL_TICKET_COUNT.intValue());

        // 최종 티켓  수량이 0인지 확인
        Ticket updatedTicket = ticketRepository.findById(ticket.getTicketId()).orElseThrow();
        assertThat(updatedTicket.getTicketCount()).isEqualTo(0);

        // 최종적으로 구매에 성공한 수량과 테스트 소요 시간 출력
        logger.info("성공적으로 구매된 티켓 수: " + successfulPurchases.get());
        logger.info("테스트 실행 소요 시간: " + elapsedTime + " ms");

        // 모든 요청 완료 후 스레드 종료
        executorService.shutdown();
    }
}
