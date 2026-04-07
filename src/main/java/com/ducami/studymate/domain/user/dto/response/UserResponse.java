package com.ducami.studymate.domain.user.dto.response;

import com.ducami.studymate.domain.user.entity.UserEntity;

public record UserResponse(
    Long userId,
    String name,
    String email
) {
    public static UserResponse from(UserEntity entity) {
        return new UserResponse(
                entity.getId(),
                entity.getName(),
                entity.getEmail()
        );
    }
}
