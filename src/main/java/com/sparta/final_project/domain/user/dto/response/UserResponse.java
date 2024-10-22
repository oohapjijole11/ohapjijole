package com.sparta.final_project.domain.user.dto.response;

import lombok.Getter;

@Getter
public class UserResponse {
    private Long userId;

    public UserResponse(Long userId) {
        this.userId = userId;
    }
}
