package com.sparta.final_project.domain.ticket.service;



import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Service
@RequiredArgsConstructor
public class SqsService {

    private final AmazonSQSAsync sqsClient;

    @Value("${cloud.aws.sqs.queue-url}")
    private String queueUrl;


    public void sendMessage(BuyTicketsRequest buyTicketsRequest) {
        String messageBody = String.format("{\"ticketId\": %d, \"userId\": %d, \"ticketNumber\": %d}",
                buyTicketsRequest.getTicketId(),
                buyTicketsRequest.getUserId(),
                buyTicketsRequest.getTicketNumber());

        SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, messageBody);
        sqsClient.sendMessage(sendMessageRequest);
    }
}