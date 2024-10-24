package com.sparta.final_project.domain.auth.dto.response;

import lombok.Getter;

@Getter
public class SignupResponseDto {

    // 회원가입 반환 값
    private final String bearerToken;

    public SignupResponseDto(String bearerToken) {
        this.bearerToken = bearerToken;
    }

}
