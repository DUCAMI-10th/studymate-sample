package com.ducami.studymate.domain.user.entity;

import com.ducami.studymate.domain.user.dto.request.SignupRequest;
import com.ducami.studymate.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@Entity(name = "tb_users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    public static UserEntity signup(SignupRequest request, String encodedPassword) {
        return UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(encodedPassword)
                .role(UserRole.USER)
                .build();
    }
}
