package com.sparta.final_project.domain.ticket.service;



import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.fasterxml.jackson.core.JsonProcessingException;

import com.fasterxml.jackson.databind.ObjectMapper;
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
    private final ObjectMapper objectMapper; // ObjectMapper를 빈으로 주입받습니다.

    @Value("${cloud.aws.sqs.queue-url}")
    private String queueUrl;

    public void sendMessage(BuyTicketsRequest buyTicketsRequest) {
        try {
            String messageBody = objectMapper.writeValueAsString(buyTicketsRequest);
            SendMessageRequest sendMessageRequest = new SendMessageRequest(queueUrl, messageBody);
            sqsClient.sendMessage(sendMessageRequest);
            System.out.println("메시지가 SQS에 전송되었습니다: " + messageBody);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("SQS 메시지 전송 중 JSON 처리 오류 발생", e);
        } catch (Exception e) {
            throw new RuntimeException("SQS 메시지 전송 중 오류 발생", e);
        }
    }
}