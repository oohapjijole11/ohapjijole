package com.sparta.final_project.domain.bid.repository;

import com.sparta.final_project.domain.auction.entity.Auction;
import com.sparta.final_project.domain.user.entity.User;
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
    public void setBid(String eventCacheId, int price) {
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

    public int findLastBidPrice(String auctionId) {
        Map<String, Object> result = (Map<String, Object>) (Map) redisTemplate.opsForHash().entries(eventkey);
        Optional<Map.Entry<String, Object>> price = result.entrySet().stream()
                .filter(entry -> ((entry.getKey()).startsWith(auctionId))).max(Map.Entry.comparingByKey());
        return price.map(entry -> {
                    // 값을 int로 안전하게 변환
                    try {
                        return Integer.parseInt(entry.getValue().toString());
                    } catch (NumberFormatException e) {
                        return null; // 형변환 실패 시 null 반환
                    }
                }).orElse(0); // null 필터링

    }

    public void setAuction(String auctionId, Auction auction) {
        redisTemplate.opsForValue().set(auctionId, auction);
    }

    public Optional<Auction> findAuction(String auctionId) {
        return Optional.ofNullable((Auction) redisTemplate.opsForValue().get(auctionId));
    }

    public void setUser(String userId, User user) {
        redisTemplate.opsForValue().set(userId,user);
    }
    public Optional<User> findUser(String userId) {
        return Optional.ofNullable((User) redisTemplate.opsForValue().get(userId));
    }

    public void setTicket(String ticketId, Boolean isHave) {
        redisTemplate.opsForValue().set(ticketId,String.valueOf(isHave));
    }
    public boolean findTicket(String ticketId) {
        Object value = redisTemplate.opsForValue().get(ticketId);
        return Objects.nonNull(value);
    }
}

