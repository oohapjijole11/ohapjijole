package com.sparta.final_project.domain.ticket.service;


import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import com.sparta.final_project.domain.ticket.dto.response.BuyTicketsResponse;
import com.sparta.final_project.domain.ticket.entity.BuyTickets;
import com.sparta.final_project.domain.ticket.repository.BuyTicketsRepository;
import com.sparta.final_project.domain.ticket.repository.TicketRepository;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;


@Service
@RequiredArgsConstructor
public class TicketBuyService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final BuyTicketsRepository buyTicketsRepository;
    private final SqsService sqsService;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 티켓 구매 요청 처리
    // 티켓 구매 요청 처리 (SQS 대기열에 추가)
    @Transactional
    public String buyTicket(AuthUser authUser, BuyTicketsRequest buyTicketsRequest) {
        // Redis에 대기열 추가
        userRepository.findById(authUser.getId()).orElseThrow(()-> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));

        String queueKey = "ticketQueue:" + buyTicketsRequest.getTicketId();
        Long position = redisTemplate.opsForList().rightPush(queueKey, authUser.getId());

        // SQS에 구매 요청 메시지 추가
        sqsService.sendMessage(authUser.getId(),buyTicketsRequest);

        // 앞에 몇 명이 있는지 확인
        Long waitingCount = position - 1;

        return "티켓 구매 요청이 대기 중입니다. 앞에 " + waitingCount + "명이 대기 중입니다.";

    }

    // 대기열에서 메시지를 주기적으로 처리하여 티켓 구매 진행
//    @Scheduled(fixedRate = 10000)
//    public void processQueueMessages() {
//        List<Message> messages = amazonSQSAsync.receiveMessage(queueUrl).getMessages();
//
//        for (Message message : messages) {
//            try {
//                BuyTicketsRequest buyTicketsRequest = objectMapper.readValue(message.getBody(), BuyTicketsRequest.class);
//
//                Ticket ticket = ticketRepository.findById(buyTicketsRequest.getTicketId())
//                        .orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FIND_TICKET));
//
//                if (ticket.getTicketCount() > 0) {
//                    User user = userRepository.findById(buyTicketsRequest.getUserId())
//                            .orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_USER));
//
//                    ticket.setTicketCount(ticket.getTicketCount() - 1);
//                    BuyTickets buyTickets = new BuyTickets(ticket, user);
//                    buyTicketsRepository.save(buyTickets);
//                    ticketRepository.save(ticket);
//
//                    System.out.println("대기열에서 티켓 구매가 완료되었습니다.");
//                } else {
//                    System.out.println("티켓이 소진되었습니다.");
//                }
//
//                // 메시지 처리 완료 후 삭제
//                amazonSQSAsync.deleteMessage(queueUrl, message.getReceiptHandle());
//            } catch (Exception e) {
//                System.err.println("대기열 메시지 처리 중 오류 발생: " + e.getMessage());
//            }
//        }
//    }

    public List<BuyTicketsResponse> getbuyticketList() {
        List<BuyTickets> buyticketList = buyTicketsRepository.findAll();
        return buyticketList.stream().map(BuyTicketsResponse::new).toList();
    }
}
