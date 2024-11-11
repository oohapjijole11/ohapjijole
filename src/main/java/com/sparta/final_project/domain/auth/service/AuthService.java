package com.sparta.final_project.domain.auth.service;

import com.sparta.final_project.config.security.JwtUtil;
import com.sparta.final_project.domain.auth.dto.request.SigninRequestDto;
import com.sparta.final_project.domain.auth.dto.request.SignupRequestDto;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.toss.entity.VirtualAccount;
import com.sparta.final_project.domain.toss.repository.VirtualAccountRepository;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.entity.UserRole;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final VirtualAccountRepository virtualAccountRepository;
    private final JwtUtil jwtUtil;

    // 이메일 유효성 검사 정규 표현식
    private static final String EMAIL_PATTERN =
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    // 비밀번호 유효성 검사 정규 표현식
    private static final String PASSWORD_PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";

    // 회원가입
    @Transactional
    public void signup(SignupRequestDto signupRequestDto) {

        // 이메일 형식 유효성 검사
        if(!isValidEmail(signupRequestDto.getEmail())) {
            throw new OhapjijoleException(ErrorCode._BAD_REQUEST_INVALID_EMAIL);
        }

        // 이메일 중복확인
        if(userRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new OhapjijoleException(ErrorCode._DUPLICATED_EMAIL);
        }

        // 비밀번호 형식 유효성 검사
        if(!isValidPassword(signupRequestDto.getPassword())) {
            throw new OhapjijoleException(ErrorCode._INVALID_PASSWORD_FORM);
        }

        String encodedPassword = passwordEncoder.encode(signupRequestDto.getPassword());

        UserRole role = UserRole.of(signupRequestDto.getUserRole());

        User newUser = new User(
                signupRequestDto.getEmail(),
                encodedPassword,
                signupRequestDto.getName(),
                role,
                signupRequestDto.getSlackUrl()
        );

//        회원가입 할때 가상계좌 자동 등록
        VirtualAccount virtualAccount = new VirtualAccount();
        virtualAccount.setUser(newUser);
        virtualAccount.setAccountNumber(generateAccountNumber());
        virtualAccount.setBalance(0.0); // Initial balance
        virtualAccountRepository.save(virtualAccount);

        newUser.setVirtualAccount(virtualAccount);
        // 유저 생성 후 저장
        userRepository.save(newUser);
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
    public String signin(SigninRequestDto signinRequestDto) {
        User user = userRepository.findByEmail(signinRequestDto.getEmail()).orElseThrow(
                () -> new OhapjijoleException(ErrorCode._BAD_REQUEST_NOT_FOUND_USER));

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환
        if(!passwordEncoder.matches(signinRequestDto.getPassword(), user.getPassword())) {
            throw new OhapjijoleException(ErrorCode._PASSWORD_NOT_MATCHES);
        }

        // 탈퇴한 유저일 경우 로그인 불가
        if(user.getIsdeleted()) {
            throw new OhapjijoleException(ErrorCode._DELETED_USER);
        }

        return jwtUtil.createToken(user.getId(), user.getEmail(), user.getName(), user.getRole());
    }

//    가상계좌 만드는 메소드
    private String generateAccountNumber() {
        String prefix = "VA";
        String randomNumber = String.format("%010d", new Random().nextInt(1_000_000_000));
        String accountNumber = prefix + randomNumber;
        while (virtualAccountRepository.existsByAccountNumber(accountNumber)) {
            randomNumber = String.format("%010d", new Random().nextInt(1_000_000_000));
            accountNumber = prefix + randomNumber;
        }
        return accountNumber;
    }

}
