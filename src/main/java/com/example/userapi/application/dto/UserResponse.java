package com.example.userapi.application.dto;

import com.example.userapi.domain.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

/**
 * ユーザーレスポンスDTO
 * パスワードは含めない
 */
public record UserResponse(
        Long id,
        String name,
        String email,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime updatedAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail().getValue(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}