package com.sparta.final_project.domain.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {

    String key(); // 락의 이름

    /**
     * 메서드의 파라미터값을 Key 에 추가로 붙일때 사용한다. Spring Expression Language (SpEL) 을 사용한다.
     * @see <a href="https://docs.spring.io/spring-framework/docs/3.0.x/reference/expressions.html">SpEl 문서</a>
     */
    String[] dynamicKey() default {};

    TimeUnit timeUnit() default TimeUnit.SECONDS;

    long waitTime() default 5L; // 락을 획됙하기 까지 대기하는 시간(시간을 초과하면 false 반환)

    long leaseTime() default 3L; // 락의 타임아웃을 설정 (시간이 지나면 락이 만료되어 해제됨)
}
