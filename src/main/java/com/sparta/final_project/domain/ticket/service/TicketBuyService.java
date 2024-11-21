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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Service
@RequiredArgsConstructor
public class TicketBuyService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final BuyTicketsRepository buyTicketsRepository;
    private final SqsService sqsService;
    private final ConcurrentMap<Long, SseEmitter> sseEmitters = new ConcurrentHashMap<>();
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Transactional
    public String buyTicket(AuthUser authUser, BuyTicketsRequest buyTicketsRequest) {
        // 사용자 확인
        userRepository.findById(authUser.getId())
                .orElseThrow(() -> new OhapjijoleException(ErrorCode._USER_NOT_FOUND));

        // Redis에 대기열 추가
        String queueKey = "ticketQueue:" + buyTicketsRequest.getTicketId();
        Long position = redisTemplate.opsForList().rightPush(queueKey, authUser.getId());

        // SQS에 구매 요청 메시지 추가
        sqsService.sendMessage(authUser.getId(), buyTicketsRequest);

        // 앞에 몇 명이 있는지 확인
        Long waitingCount = position - 1;

        // SSE 연결이 존재하면 대기열 상태 전송
        sendQueueStatusToClient(authUser.getId(), waitingCount);

        return "티켓 구매 요청이 대기 중입니다. 앞에 " + waitingCount + "명이 대기 중입니다.";
    }

    // SSE 구독
    public SseEmitter subscribeQueueStatus(Long userId) {
        SseEmitter sseEmitter = new SseEmitter();
        sseEmitters.put(userId, sseEmitter);

        // SSE 연결이 닫힐 때 맵에서 제거
        sseEmitter.onCompletion(() -> sseEmitters.remove(userId));
        sseEmitter.onTimeout(() -> sseEmitters.remove(userId));

        return sseEmitter;
    }

    // 대기열 상태를 실시간으로 전송
    private void sendQueueStatusToClient(Long userId, Long waitingCount) {
        SseEmitter sseEmitter = sseEmitters.get(userId);
        if (sseEmitter != null) {
            try {
                sseEmitter.send("대기 중입니다. 앞에 " + waitingCount + "명이 있습니다.");
            } catch (IOException e) {
                sseEmitters.remove(userId);
            }
        }
    }

    public List<BuyTicketsResponse> getbuyticketList() {
        List<BuyTickets> buyticketList = buyTicketsRepository.findAll();
        return buyticketList.stream().map(BuyTicketsResponse::new).toList();
    }
}
