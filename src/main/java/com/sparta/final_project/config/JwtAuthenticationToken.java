package com.sparta.final_project.config;

import com.sparta.final_project.domain.user.entity.UserRole;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

// ArgumentResolver 역홣을 대신해줌
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthUser authUser;

    public JwtAuthenticationToken(AuthUser authUser) {
        // 부모 클래스가 필요로 하는 권한 정보(GrantedAuthority 타입)
        super(convertToAuthorities(authUser.getRole()));
        this.authUser = authUser;
        setAuthenticated(true); // 인증 완료로 설정
    }

    @Override
    public Object getCredentials() {
        return null; // 자격증명 정보, 이미 토큰으로 증명되므로 null
    }

    @Override
    public Object getPrincipal() {
        return authUser; // 인증된 사용자 정보 반환
    }

    // UserRole을 GrantedAuthority로 변화하는 메소드
    private static List<SimpleGrantedAuthority> convertToAuthorities(UserRole role) {
        return Collections.singletonList(new SimpleGrantedAuthority(role.name()));
    }

}
