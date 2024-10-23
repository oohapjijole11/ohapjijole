package com.sparta.final_project.domain.user.entity;

import com.sparta.final_project.domain.common.entity.ErrorStatus;
import com.sparta.final_project.domain.common.exception.ApiException;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum UserRole {
    USER, ADMIN;

    public static UserRole of(String role) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new ApiException(ErrorStatus._USER_ROLE_IS_NULL));
    }

    public static class Authority {
        public static final String USER = "ROLE_USER";

        public static final String ADMIN = "ROLE_ADMIN";
    }
}
