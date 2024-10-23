package com.sparta.final_project.config.security;

import com.sparta.final_project.domain.user.entity.UserRole;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthUser {

    // 토큰에 유저정보를 가져와서 권한을 확인하기 위한 자료
    // 인증하는곳에서는 중요한 정보를 넣으면 안됌
    // 로그인정보
    private final Long id;

    private final String name;

    private final String email;

    private final Collection<? extends GrantedAuthority> authorities;

    public AuthUser(Long id, String name, String email, UserRole role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.authorities = List.of(new SimpleGrantedAuthority(role.name()));
    }
}
