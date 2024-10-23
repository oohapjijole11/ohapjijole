import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import com.sparta.final_project.domain.ticket.service.BuyTicketsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@SpringBootTest
public class TicketConcurrencyTest {

    @Autowired
    private BuyTicketsService buyTicketsService;

    @Test
    @Transactional
    public void testMultipleUsersBuyingTickets() throws InterruptedException {
        // 쓰레드를 통해 여러 사용자가 동시에 티켓을 구매하는 상황을 시뮬레이션
        ExecutorService executorService = Executors.newFixedThreadPool(10); // 10명의 사용자 동시 실행
        Long ticketId = 1L; // 테스트할 티켓 ID
        Long userId = 1L; // 사용자 ID (테스트 용도)
        Long ticketNumber = 1L; // 구매할 티켓 수량

        for (int i = 0; i < 10; i++) {
            int userNumber = i;
            executorService.submit(() -> {
                BuyTicketsRequest request = new BuyTicketsRequest(ticketId, userId + userNumber, ticketNumber);
                try {
                    buyTicketsService.buyTicket(request);
                    System.out.println("User " + (userId + userNumber) + " purchased a ticket successfully.");
                } catch (Exception e) {
                    System.err.println("User " + (userId + userNumber) + " failed to purchase a ticket: " + e.getMessage());
                }
            });
        }

        // 모든 쓰레드가 완료될 때까지 기다립니다.
        executorService.shutdown();
        executorService.awaitTermination(30, TimeUnit.SECONDS);
    }
}