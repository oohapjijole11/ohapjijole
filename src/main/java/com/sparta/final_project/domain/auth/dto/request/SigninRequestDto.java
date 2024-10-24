package com.sparta.final_project.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SigninRequestDto {

    // 로그인 요청 값
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

}
