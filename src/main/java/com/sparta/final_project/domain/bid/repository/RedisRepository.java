package com.sparta.final_project.domain.bid.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RedisRepository {

    private final RedisTemplate<String, Object> redisTemplate;

    private final String eventkey = "bid";

    //event저장
    public void setbid(String eventCacheId, int price) {
//        redisTemplate.opsForValue().set(eventCacheId, price);
        redisTemplate.opsForHash().put(eventkey, eventCacheId, String.valueOf(price));
    }

    //해당 경매장의 모든 event
    public Map<String, Object> findAllEventWithAuctionId(String auctionId) {
        Map<Object, Object> result = redisTemplate.opsForHash().entries(eventkey);
        return result.entrySet().stream()
                .filter(entry->(((String)entry.getKey()).startsWith(auctionId)))
                .collect(Collectors.toMap(entry->(String)entry.getKey(), Map.Entry::getValue));
    }

    public int findlastBidprice(String auctionId) {
        Map<String, Object> result = (Map<String, Object>) (Map) redisTemplate.opsForHash().entries(eventkey);
        Optional<Map.Entry<String, Object>> price = result.entrySet().stream()
                .filter(entry->((entry.getKey()).startsWith(auctionId)))
                .sorted(Map.Entry.<String, Object>comparingByKey().reversed())
                .findFirst();
        return price.map(entry -> {
                    // 값을 int로 안전하게 변환
                    try {
                        return Integer.parseInt(entry.getValue().toString());
                    } catch (NumberFormatException e) {
                        return null; // 형변환 실패 시 null 반환
                    }
                })
                .filter(Objects::nonNull).orElse(0); // null 필터링

    }

    //낙찰때 쓸 예정
    public void deleteAllEventStartWithAuctionId(String auctionId) {
        redisTemplate.opsForHash().delete(eventkey, auctionId);
    }
}

