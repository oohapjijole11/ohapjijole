package com.sparta.final_project.domain.user.service;


import com.sparta.final_project.config.security.AuthUser;
import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import com.sparta.final_project.domain.user.dto.request.DeleteUserRequestDto;
import com.sparta.final_project.domain.user.entity.User;
import com.sparta.final_project.domain.user.entity.UserRole;
import com.sparta.final_project.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    // 회원 탈퇴
    @Transactional
    public void deletedUser(AuthUser authUser, DeleteUserRequestDto deleteUserRequestDto) {

        // authUser 이메일로 현재 로그인한 User 찾기
        System.out.print("AuthUserEmail: " + authUser.getEmail());
        User user = userRepository.findByEmail(authUser.getEmail())
                .orElseThrow(()-> new OhapjijoleException(ErrorCode._NOT_FOUND_USER));

        // 회원의 비밀번호가 일치하는지 확인
        if(!passwordEncoder.matches(deleteUserRequestDto.getPassword(), user.getPassword())) {
            throw new OhapjijoleException(ErrorCode._PASSWORD_NOT_MATCHES);
        }

        // 이미 탈퇴한 회원인지 확인
        if(user.getIsdeleted()) {
            throw new OhapjijoleException(ErrorCode._DELETED_USER);
        }

        // 회원 탈퇴 메소드
        user.deletedUser(user.getEmail(), user.getPassword());

        // 변경된 내용 ㅈ장
        userRepository.save(user);
    }

    @Transactional
    public void updateUserAuthority(Long id, AuthUser authUser) {
        if(!authUser.getAuthorities().equals(UserRole.ADMIN)) {
            throw new OhapjijoleException(ErrorCode._NOT_PERMITTED_USER);
        }
        User user = userRepository.findById(id).orElseThrow(()-> new OhapjijoleException(ErrorCode._NOT_FOUND_USER));

        if(user.getIsdeleted()) {
            throw new OhapjijoleException(ErrorCode._DELETED_USER);
        }
        user.updateUserRole(UserRole.ADMIN);
    }

}
