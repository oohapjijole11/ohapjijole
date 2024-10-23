package com.sparta.final_project.domain.user.entity;

import com.sparta.final_project.domain.common.exception.ErrorCode;
import com.sparta.final_project.domain.common.exception.OhapjijoleException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserRole {
    USER, ADMIN;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new OhapjijoleException(ErrorCode._USER_ROLE_IS_NULL));
    }
}
