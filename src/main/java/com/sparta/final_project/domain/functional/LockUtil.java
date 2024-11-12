package com.sparta.final_project.domain.functional;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class LockUtil {

    private final RedissonClient redissonClient;

    public LockUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    public <T> T executeWithLock(String key, long waitTime, long leaseTime,
                                 TimeUnit unit, LockableTask<T> task) throws Throwable {
        String lockKey = "LOCK: " + key;
        RLock rLock = redissonClient.getLock(lockKey);

        try {
            // 지정된 시간 내 락 획득 시도
            boolean lockAcquired = rLock.tryLock(waitTime, leaseTime, unit);
            if(!lockAcquired) {
                throw new RuntimeException("락을 획득할 수 없습니다: " + lockKey);
            }

            // 락을 획득한 경우
            return task.execute();
        } catch(InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("락 작업이 중단 되었습니다: " + lockKey, e);
        } finally {
            // 락 해제
            if(rLock.isHeldByCurrentThread()) {
                rLock.unlock();
            }
        }
    }

}
