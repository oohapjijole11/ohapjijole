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
                context.getLogger().log("처리 중인 메시지: " + record.getBody());
                BuyTicketsRequest buyTicketsRequest = objectMapper.readValue(record.getBody(), BuyTicketsRequest.class);

                // 티켓 구매 로직 호출 전 로그
                context.getLogger().log("buyTicket() 호출 중, ticketId: " + buyTicketsRequest.getTicketId());

                // TicketBuyService의 buyTicket 메서드 호출
                String responseMessage = ticketBuyService.buyTicket(buyTicketsRequest);

                // 티켓 구매 성공 후 로그
                context.getLogger().log("ticketId: " + buyTicketsRequest.getTicketId() + "에 대한 티켓 구매가 완료되었습니다. 응답: " + responseMessage);

            } catch (Exception e) {
                context.getLogger().log("메시지 처리 중 오류 발생: " + e.getMessage());
            }
        });
        return "메시지 처리 완료."; // 메시지 처리 결과 반환
    }
}
