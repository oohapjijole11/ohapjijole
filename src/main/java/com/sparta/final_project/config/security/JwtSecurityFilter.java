package com.sparta.final_project.config.security;

import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(
            // JWT 토큰 검증, 사용자 인증 확인
            HttpServletRequest httpRequest,
            @NonNull HttpServletResponse httpResponse,
            @NonNull FilterChain chain
    ) throws ServletException, IOException {
        // Authorization 헤더 = 토큰을 담을 곳
        String authorizationHeader = httpRequest.getHeader("Authorization");
        // 헤더 있는지 확인 동시에 토큰이 Bearer 로 시작하니까 그 부분 확인.
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = jwtUtil.substringToken(authorizationHeader);
            try {
                // 토큰에서 추출
                Claims claims = jwtUtil.extractClaims(jwt); // 토큰의 주체
                Long userId = Long.parseLong(claims.getSubject());
                String name = claims.get("name",String.class);
                String email = claims.get("email", String.class);
                UserRole userRole = UserRole.of(claims.get("role", String.class));


                // 인증
                if (SecurityContextHolder.getContext().getAuthentication() == null) {
                    AuthUser authUser = new AuthUser(userId, name, email, userRole);

                    JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(authUser);
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }

                // 예외 처리
            } catch (SecurityException | MalformedJwtException e) {
                throw new OhapjijoleException(ErrorCode._UNAUTHORIZED_INVALID_TOKEN);
            } catch (ExpiredJwtException e) {
                throw new OhapjijoleException(ErrorCode._UNAUTHORIZED_EXPIRED_TOKEN);
            } catch (UnsupportedJwtException e) {
                throw new OhapjijoleException(ErrorCode._BAD_REQUEST_UNSUPPORTED_TOKEN);
            }
        }
        chain.doFilter(httpRequest, httpResponse);
    }


}
