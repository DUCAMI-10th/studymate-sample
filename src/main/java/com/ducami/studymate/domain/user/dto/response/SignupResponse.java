package com.ducami.studymate.domain.user.dto.response;

import com.ducami.studymate.domain.user.entity.UserEntity;
import com.ducami.studymate.domain.user.enums.UserRole;

public record SignupResponse(
        Long id,
        String name,
        String email,
        UserRole role
) {
    public static SignupResponse from(UserEntity user) {
        return new SignupResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
