package com.sparta.final_project.config;

import com.sparta.final_project.domain.user.entity.UserRole;
import lombok.Getter;

@Getter
public class AuthUser {

    // 토큰에 유저정보를 가져와서 권한을 확인하기 위한 자료
    // 인증하는곳에서는 중요한 정보를 넣으면 안됌
    // 로그인정보
    private final Long userId;

    private final String name;

    private final String email;

    private final UserRole role;

//    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long userId, String name, String email, UserRole role) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.role = role;
    }

}
