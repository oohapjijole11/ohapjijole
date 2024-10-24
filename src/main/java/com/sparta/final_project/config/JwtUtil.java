package com.sparta.final_project.config;


import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {

    private final String BEARER_PREFIX = "Bearer ";
    private final long TOKEN_TIME = 60 * 60 * 1000L; // 토큰 유효시간 60분

    @Value("${JWT_SECRET_KEY}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // 토큰생성
    public String createToken(Long userId, String name, String email, UserRole role) {
        Date date = new Date();

        // 토큰에 BEARER 추가해서 반환, 총 7자
        return BEARER_PREFIX +
                Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("name", name)
                        .claim("email", email)
                        .claim("role", role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 토큰 만료기간
                        .setIssuedAt(date) // 발급일
                        .signWith(key, signatureAlgorithm) // 암호화 알고리짐
                        .compact();
    }

    // 토큰 추출 7자리 쳐내기
     public String substringToken(String tokenValue) {
        if(StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
            return tokenValue.substring(7);
        }
        throw new OhapjijoleException(ErrorCode._NOT_FOUND_TOKEN); // 토큰이 없거나 유효하지 않을 때
     }

     public Claims extractClaims(String token) {
        return Jwts.parserBuilder() // JwtParseBuilder 생성
                .setSigningKey(key) // 키 설정
                .build() // jwtParser 빌드
                .parseClaimsJws(token) // 빌드된 JwtParser 에서 parseClaimsJws 호출
                .getBody(); // Claims 추출

     }
}
