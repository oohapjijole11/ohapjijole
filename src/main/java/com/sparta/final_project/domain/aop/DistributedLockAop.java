package com.sparta.final_project.domain.aop;

import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class DistributedLockAop {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;

    @Pointcut("@annotation(com.sparta.final_project.domain.aop.DistributedLock)")
    private void distributedLock() {}

    @Around("distributedLock()")
    public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
        final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        final Method method = signature.getMethod();
        final String[] parameterNames = signature.getParameterNames();
        final Object[] args = joinPoint.getArgs();
        final DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        // 복합 키, 단일 키 모두 지원
        final List<String> keys =
                DistributedLockKeyGenerator.generateKeys(
                        distributedLock.key(),
                        distributedLock.dynamicKey(),
                        parameterNames,
                        args
                );

        // Redisson의 RLock 사용하여 분산 락 설정
        for (String key : keys) {
            String lockKey = REDISSON_LOCK_PREFIX + key;
            RLock rLock = redissonClient.getLock(lockKey);
            log.info("lock inf [method: {}] [key: {}]", method, lockKey);

            try{
                boolean lockAcquired = rLock.tryLock(
                        distributedLock.waitTime(),
                        distributedLock.leaseTime(),
                        distributedLock.timeUnit()
                );
                log.info("락 흭득 : {}", lockKey);

                if(!lockAcquired) {
                    throw new OhapjijoleException(ErrorCode._LOCK_NOT_AVAILABLE_ERROR);
                }

                // 락이 설공적으로 걸렸다면, 비즈니스 로직 실행
                return joinPoint.proceed();
            } catch (InterruptedException e) {
                throw new OhapjijoleException(ErrorCode._LOCK_INTERRUPTED_ERROR);
            } finally {
                // 락 해제
                try {
                    rLock.unlock();
                    log.info("락 잠금 해제 [key: {}]", lockKey);
                } catch (IllegalMonitorStateException e) {
                    log.info("락이 이미 해제 되었거나, 헌제 해결할 수 없습니다[key: {}]", lockKey);
                }
            }
        }
        return null;
    }
}
