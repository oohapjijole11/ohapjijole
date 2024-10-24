package com.sparta.final_project.config.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;

// ArgumentResolver 역홣을 대신해줌
public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final AuthUser authUser;

    public JwtAuthenticationToken(AuthUser authUser) {
        // 부모 클래스가 필요로 하는 권한 정보(GrantedAuthority 타입)
        super(authUser.getAuthorities());
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
}
