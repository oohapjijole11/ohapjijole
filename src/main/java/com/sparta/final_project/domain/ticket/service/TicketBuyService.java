package com.sparta.final_project.domain.ticket.service;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import com.sparta.final_project.domain.ticket.dto.response.BuyTicketsResponse;
import com.sparta.final_project.domain.ticket.entity.BuyTickets;
import com.sparta.final_project.domain.ticket.entity.Ticket;
import com.sparta.final_project.domain.ticket.repository.BuyTicketsRepository;
import com.sparta.final_project.domain.ticket.repository.TicketRepository;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TicketBuyService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final BuyTicketsRepository buyTicketsRepository;
    private final AmazonSQSAsync amazonSQSAsync;
    private final RedissonClient redissonClient;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final SqsService sqsService;

    @Value("${cloud.aws.sqs.queue-url}")
    private String queueUrl;



    // 티켓 구매 요청 처리
    // 티켓 구매 요청 처리 (SQS 대기열에 추가)
    @Transactional
    public String buyTicket(BuyTicketsRequest buyTicketsRequest) {
        String lockKey = "ticket:" + buyTicketsRequest.getTicketId();
        RLock lock = redissonClient.getLock(lockKey);

        try {
            if (lock.tryLock(3, 5, TimeUnit.SECONDS)) {
                try {
                    Ticket ticket = ticketRepository.findById(buyTicketsRequest.getTicketId())
                            .orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FIND_TICKET));

                    if (ticket.getTicketCount() == 0) {
                        throw new OhapjijoleException(ErrorCode._NOT_ENOUGH_TICKET);
                    }

                    // 즉시 구매 로직 제거, 무조건 SQS 대기열에 추가
                    sqsService.sendMessage(buyTicketsRequest);
                    return "티켓 구매 요청이 대기 중입니다. 구매 가능 시 자동으로 처리됩니다.";
                } finally {
                    lock.unlock();
                }
            } else {
                return "다른 사용자가 티켓을 구매 중입니다. 잠시만 기다려 주세요.";
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("티켓 구매 요청 중 오류가 발생했습니다.", e);
        }
    }

    // 대기열에서 메시지를 주기적으로 처리하여 티켓 구매 진행
    @Scheduled(fixedRate = 10000)
    public void processQueueMessages() {
        List<Message> messages = amazonSQSAsync.receiveMessage(queueUrl).getMessages();

        for (Message message : messages) {
            try {
                BuyTicketsRequest buyTicketsRequest = objectMapper.readValue(message.getBody(), BuyTicketsRequest.class);

                Ticket ticket = ticketRepository.findById(buyTicketsRequest.getTicketId())
                        .orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FIND_TICKET));

                if (ticket.getTicketCount() > 0) {
                    User user = userRepository.findById(buyTicketsRequest.getUserId())
                            .orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_USER));

                    ticket.setTicketCount(ticket.getTicketCount() - 1);
                    BuyTickets buyTickets = new BuyTickets(ticket, user);
                    buyTicketsRepository.save(buyTickets);
                    ticketRepository.save(ticket);

                    System.out.println("대기열에서 티켓 구매가 완료되었습니다.");
                } else {
                    System.out.println("티켓이 소진되었습니다.");
                }

                // 메시지 처리 완료 후 삭제
                amazonSQSAsync.deleteMessage(queueUrl, message.getReceiptHandle());
            } catch (Exception e) {
                System.err.println("대기열 메시지 처리 중 오류 발생: " + e.getMessage());
            }
        }
    }

    public List<BuyTicketsResponse> getbuyticketList() {
        List<BuyTickets> buyticketList = buyTicketsRepository.findAll();
        return buyticketList.stream().map(BuyTicketsResponse::new).toList();
    }
}
