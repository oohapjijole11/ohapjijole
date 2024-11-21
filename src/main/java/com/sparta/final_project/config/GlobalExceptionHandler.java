package com.sparta.final_project.config;

import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * @param ex : 오류상태, 메시지
     * @return 해당 내용이 담긴 에러 객체 반환
     */
    @ExceptionHandler(OhapjijoleException.class)
    public ResponseEntity<Map<String, Object>> handlePasswordMismatchException(OhapjijoleException ex) {
        return getErrorResponse(ex.getErrorCode().getStatus(), ex.getMessage());
    }

    /**
     * @param status  : 오류 상태 코드
     * @param message : 오류 메시지
     * @return 해당 내용이 담긴 에러 ResponseEntity 객체
     */
    public ResponseEntity<Map<String, Object>> getErrorResponse(HttpStatus status, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", status.name());
        errorResponse.put("code", status.value());
        errorResponse.put("message", message);
        log.warn("OhapjijoleException ::: {}", message);
        return new ResponseEntity<>(errorResponse, status);
    }
}
