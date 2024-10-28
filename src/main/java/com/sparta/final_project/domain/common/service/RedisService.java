package com.sparta.final_project.domain.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // 키에 해당하는 값을 저장
    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 키에 해당하는 값을 조회
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 키에 해당하는 값을 삭제
    public void deleteValue(String key) {
        redisTemplate.delete(key);
    }

    // 키가 존재하는지 확인
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }
}
