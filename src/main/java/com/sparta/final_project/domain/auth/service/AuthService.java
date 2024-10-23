package com.sparta.final_project.domain.auth.service;

import com.sparta.final_project.domain.auth.dto.request.SigninRequestDto;
import com.sparta.final_project.domain.auth.dto.response.SigninResponseDto;
import com.sparta.final_project.domain.common.entity.ErrorStatus;
import com.sparta.final_project.config.JwtUtil;
import com.sparta.final_project.domain.auth.dto.request.SignupRequestDto;
import com.sparta.final_project.domain.auth.dto.response.SignupResponseDto;
import com.sparta.final_project.domain.common.exception.ApiException;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.entity.UserRole;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 이메일 유효성 검사 정규 표현식
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // 비밀번호 유효성 검사 정규 표현식
    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";


    // 회원가입
    @Transactional
    public SignupResponseDto signup(SignupRequestDto signupRequestDto) {

        // 이메일 형식 유효성 검사
        if(!isValidEmail(signupRequestDto.getEmail())) {
            throw new ApiException(ErrorStatus._BAD_REQUEST_INVALID_EMAIL);
        }

        // 이메일 중복확인
        if(userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new ApiException(ErrorStatus._DUPLICATED_EMAIL);
        }

        // 비밀번호 형식 유효성 검사
        if(!isValidPassword(signupRequestDto.getPassword())) {
            throw new ApiException(ErrorStatus._INVALID_PASSWORD_FORM);
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        UserRole role = UserRole.of(signupRequestDto.getUserRole());

        User newUser = new User(
                signupRequestDto.getEmail(),
                encodedPassword,
                signupRequestDto.getName(),
                role
        );

        // 유저 생성 후 저장
        User savedUser = userRepository.save(newUser);

        // 유저 정보를 가지고 토큰을 생성
        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getName(), savedUser.getEmail(), role);

        return new SignupResponseDto(bearerToken);
    }

    // 이메일 유효성 검사 메서드
    private boolean isValidEmail(String email) {
        return Pattern.matches(EMAIL_PATTERN, email);
    }

    // 비밀번호 유효성 검사 메서드
    private boolean isValidPassword(String password) {
        return Pattern.matches(PASSWORD_PATTERN, password);
    }

    // 로그인
    @Transactional
    public SigninResponseDto signin(SigninRequestDto signinRequestDto) {
        User user = userRepository.findByEmail(signinRequestDto.getEmail()).orElseThrow(
                () -> new ApiException(ErrorStatus._BAD_REQUEST_NOT_FOUND_USER));

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환
        if(!passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())) {
            throw new ApiException(ErrorStatus._PASSWORD_NOT_MATCHES);
        }

        // 탈퇴한 유저일 경우 로그인 불가
        if(user.getIsdeleted()) {
            throw new ApiException(ErrorStatus._DELETED_USER);
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getName(), user.getEmail(), user.getRole());

        return new SigninResponseDto(bearerToken);
    }

}
