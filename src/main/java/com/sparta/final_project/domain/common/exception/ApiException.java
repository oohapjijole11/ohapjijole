package com.sparta.final_project.domain.common.exception;

import com.sparta.final_project.domain.common.entity.BaseCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException{
    private final BaseCode errorCode;

    public ApiException(BaseCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ApiException(BaseCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

}
