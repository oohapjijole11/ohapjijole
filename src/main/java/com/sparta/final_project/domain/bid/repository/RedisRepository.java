package com.sparta.final_project.domain.bid.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.Map;
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

//    public int findlastBidprice(String auctionId) {
//        Map<Object, Object> result = redisTemplate.opsForHash().entries(eventkey);
//        int price = result.entrySet().stream()
//                .filter(entry->(((String)entry.getKey()).startsWith(auctionId)))
//                .sorted(Map.Entry.<Object, Object>comparingByKey().reversed())
//                .findFirst().get().getValue();
//        return price;
//
//    }

    //낙찰때 쓸 예정
    public void deleteAllEventStartWithAuctionId(String auctionId) {
        redisTemplate.opsForHash().delete(eventkey, auctionId);
    }
}

