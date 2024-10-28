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
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class TicketBuyService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final BuyTicketsRepository buyTicketsRepository;
    private final AmazonSQSAsync amazonSQSAsync;

    @Value("${cloud.aws.sqs.queue-url}")
    private String queueUrl;

    public void sendBuyTicketRequest(BuyTicketsRequest buyTicketsRequest) {
        amazonSQSAsync.sendMessage(new SendMessageRequest(queueUrl, buyTicketsRequest.toString()));
    }

    @Transactional
    public String buyTicket(BuyTicketsRequest buyTicketsRequest) {
        Ticket ticket = ticketRepository.findById(buyTicketsRequest.getTicketId())
                .orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FIND_TICKET));
        // 사용자 찾기
        User user = userRepository.findById(buyTicketsRequest.getUserId())
                .orElseThrow(() -> new OhapjijoleException(ErrorCode._NOT_FOUND_USER));

        // 티켓 수량 확인
        if (ticket.getTicketCount() == 0) {
            throw new OhapjijoleException(ErrorCode._NOT_ENOUGH_TICKET);
        }
        // 티켓 수량 감소
        ticket.setTicketCount(ticket.getTicketCount() - 1);

        BuyTickets buyTickets = new BuyTickets(buyTicketsRequest, ticket, user);

        buyTicketsRepository.save(buyTickets);
        ticketRepository.save(ticket);  // 티켓 수 업데이트

        // SQS에 메시지를 전송
        sendBuyTicketRequest(buyTicketsRequest);

        // 성공 메시지 반환
        return "티켓 구매가 완료되었습니다.";
    }
    //구매 티켓 다건 조회
    public List<BuyTicketsResponse> getbuyticketList() {
        List<BuyTickets> buyticketList = buyTicketsRepository.findAll();
        return buyticketList.stream().map(BuyTicketsResponse::new).toList();
    }

    public void receiveMessages() {
        // 메시지 수신
        List<Message> messages = amazonSQSAsync.receiveMessage(queueUrl).getMessages();

        for (Message message : messages) {
            try {
                // 메시지 본문 처리
                BuyTicketsRequest buyTicketsRequest = parseMessage(message.getBody());
                // 티켓 구매 처리
                buyTicket(buyTicketsRequest);
                // 처리 후 메시지 삭제
                amazonSQSAsync.deleteMessage(queueUrl, message.getReceiptHandle());
            } catch (Exception e) {
                // 에러 처리
                System.err.println("메세지처리중 오류 발생 " + e.getMessage());
            }
        }
    }

    private BuyTicketsRequest parseMessage(String messageBody) {
        // ObjectMapper를 사용하여 메시지를 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(messageBody, BuyTicketsRequest.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(fixedRate = 5000) // 5초마다 호출
    public void pollMessages() {
        receiveMessages();
    }

}
