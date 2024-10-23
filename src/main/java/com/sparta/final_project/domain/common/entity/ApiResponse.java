package com.sparta.final_project.domain.common.entity;

import com.sparta.final_project.domain.common.entity.ErrorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ApiResponse<T> {

    private final String message;
    private final Integer statusCode;
    private final T data;


    public static <T> ApiResponse<T> createSuccess(String message,Integer statusCode,T data) {

        return new ApiResponse<>(message,statusCode,data);

    }
    public static <T> ApiResponse<T> createError(String message,Integer statusCode) {
        return new ApiResponse<>(message,statusCode,null);
    }

    public static <T> ApiResponse<T> onSuccess(T result) {
        return new ApiResponse<>("Ok", 200, result);
    }

    public static ApiResponse<String> onFailure(ErrorStatus errorStatus) {
        return new ApiResponse<>(errorStatus.getMessage(), errorStatus.getStatusCode(), null);
    }
}