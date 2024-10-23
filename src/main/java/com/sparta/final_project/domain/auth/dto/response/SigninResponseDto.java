package com.sparta.final_project.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SigninResponseDto {

    // 로그인 반환 값
    private final String bearerToken;

    public SigninResponseDto(String bearerToken) {
        this.bearerToken = bearerToken;
    }

}
