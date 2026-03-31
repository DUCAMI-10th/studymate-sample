package com.ducami.studymate.domain.auth.dto.response;

import com.ducami.studymate.global.security.jwt.JwtToken;

public record TokenResponse(
        String accessToken
) {
    public static TokenResponse from(JwtToken jwtToken) {
        return new TokenResponse(jwtToken.accessToken());
    }
}
