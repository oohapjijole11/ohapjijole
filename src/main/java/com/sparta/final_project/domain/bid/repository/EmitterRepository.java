package com.sparta.final_project.domain.bid.repository;

import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Repository
@NoArgsConstructor
public class EmitterRepository {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Map<String, Object> eventCache = new ConcurrentHashMap<>();

    public SseEmitter save(String emitterId, SseEmitter sseEmitter) {
        emitters.put(emitterId, sseEmitter);
        return sseEmitter;
    }

    public Map<String, SseEmitter> findAllEmitterStartWithByAuctionId(String auctionId) {
        return emitters.entrySet().stream()
                .filter(entry -> entry.getKey().startsWith(auctionId))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void deleteById(String id) {
        emitters.remove(id);
    }

    public void deleteAllEmitterStartWithAuctionId(String auctionId) {
        emitters.forEach(
                (key, emitter) -> {
                    if (key.startsWith(auctionId)) {
                        emitter.complete();  // 연결을 명시적으로 종료
                        emitters.remove(key);
                    }
                }
        );
    }

    public Map<String, Object> findEventsSinceLastId(String lastEventId) {
        return eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().compareTo(lastEventId) > 0)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public void cleanUpOldEvents(String lastEventId) {
        eventCache.entrySet().stream()
                .filter(entry -> entry.getKey().compareTo(lastEventId) < 0)
                .forEach(Map.Entry::getValue);
    }
}
