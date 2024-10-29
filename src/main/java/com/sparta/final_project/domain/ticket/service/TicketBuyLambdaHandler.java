package com.sparta.final_project.domain.ticket.service;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TicketBuyLambdaHandler implements RequestHandler<SQSEvent, String> {

    private final TicketBuyService ticketBuyService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String handleRequest(SQSEvent event, Context context) {
        event.getRecords().forEach(record -> {
            try {
                // 메시지 내용 출력
                context.getLogger().log("Processing message: " + record.getBody());
                BuyTicketsRequest buyTicketsRequest = objectMapper.readValue(record.getBody(), BuyTicketsRequest.class);

                // 티켓 구매 로직 호출 전 로그
                context.getLogger().log("Calling buyTicket() with ticketId: " + buyTicketsRequest.getTicketId());

                ticketBuyService.buyTicket(buyTicketsRequest);  // 티켓 구매 로직 호출

                // 티켓 구매 성공 후 로그
                context.getLogger().log("Ticket purchase completed for ticketId: " + buyTicketsRequest.getTicketId());

            } catch (Exception e) {
                context.getLogger().log("Error processing message: " + e.getMessage());
            }
        });
        return "Messages processed successfully.";
    }
}
