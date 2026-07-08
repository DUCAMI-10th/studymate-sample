package com.ducami.studymate.domain.user.dto.response;

import com.ducami.studymate.domain.user.entity.UserEntity;
import com.ducami.studymate.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignupResponse {
    private Long id;
    private String name;
    private String email;
    private UserRole role;

    public static SignupResponse toEntity(UserEntity user) {
        return new SignupResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }
}
