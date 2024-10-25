package com.sparta.final_project.domain.ticket.service;

import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class TicketBuyProducer {
    @Autowired
    private KafkaTemplate<String, BuyTicketsRequest> kafkaTemplate;

    private final String TOPIC = "buy-tickets-topic";

    // 티켓 구매 요청을 카프카에 보냅니다.
    public void sendBuyRequest(BuyTicketsRequest buyTicketsRequest) {
        kafkaTemplate.send(TOPIC, buyTicketsRequest);
        System.out.println("Kafka 메시지가 전송되었습니다: " + buyTicketsRequest);

    }
}
