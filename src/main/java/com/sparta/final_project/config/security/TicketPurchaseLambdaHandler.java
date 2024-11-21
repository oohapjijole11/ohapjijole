//package com.sparta.final_project.config.security;
//
//import com.amazonaws.services.lambda.runtime.Context;
//import com.amazonaws.services.lambda.runtime.RequestHandler;
//import com.amazonaws.services.lambda.runtime.events.SQSEvent;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sparta.final_project.domain.ticket.dto.request.BuyTicketsRequest;
//import com.sparta.final_project.domain.ticket.service.TicketBuyService;
//import lombok.RequiredArgsConstructor;
//
//@RequiredArgsConstructor
//public class TicketPurchaseLambdaHandler implements RequestHandler<SQSEvent, Void> {
//
//    private final TicketBuyService ticketBuyService;
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public Void handleRequest(SQSEvent event, Context context) {
//        for (SQSEvent.SQSMessage message : event.getRecords()) {
//            try {
//                BuyTicketsRequest buyTicketsRequest = objectMapper.readValue(message.getBody(), BuyTicketsRequest.class);
//                ticketBuyService.processBuyTicket(buyTicketsRequest);
//                // 메시지 처리 완료 후 삭제 (이 로직은 Lambda가 아닌 SQS에서 처리됨)
//            } catch (Exception e) {
//                System.err.println("SQS 메시지 처리 중 오류 발생: " + e.getMessage());
//            }
//        }
//        return null;
//    }
//}
