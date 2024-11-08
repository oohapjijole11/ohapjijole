package com.sparta.final_project.domain.aop;

import com.sparta.final_project.config.exception.DistributedLockKeyGeneratorException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * DistributedLock과 함께 파싱하기 위한 클래스
 */
public class DistributedLockKeyGenerator {

    private static final String DELIMITER = ":";

    // 객체 생성을 막기 위해 생성자를 public 이 아닌 private로 설정
    private DistributedLockKeyGenerator() {}

    public static Object generator(String[] parameterNames, Object[] args, String key) {
        SpelExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();

        for(int i = 0; i<parameterNames.length; i++) {
            context.setVariable(parameterNames[i], args[i]);
        }
        return parser.parseExpression(key).getValue(context, Object.class);
    }

    // generateKeys 메서드를 추가 하여 여러 키 처리
    public static List<String> generateKeys(final String key,
                                            final String[] spelExpressions,
                                            final String[] parameterNames,
                                            final Object[] args) throws DistributedLockKeyGeneratorException {

        /**
         *
         */
        if(spelExpressions.length == 0) {
            return Collections.singletonList(key);
        }
        List<String> keys = Arrays.stream(spelExpressions)
                .map(spelExpression -> generateKey(key, spelExpression, parameterNames, args))
                .toList();

        if(keys.isEmpty()) {
            throw new DistributedLockKeyGeneratorException();
        }
return keys;
    }

    // generateKey 단일 키 생성
    public static String generateKey(final String key,
                                     final String spelExpression,
                                     final String[] parameterNames,
                                     final Object[] args) {
        Object dynamicKey = generator(parameterNames, args, spelExpression);
        return key + DELIMITER + dynamicKey.toString();
    }

}
