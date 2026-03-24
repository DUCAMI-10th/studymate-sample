package com.ducami.studymate.global.security.jwt;

public record JwtToken(
        String accessToken,
        String refreshToken
) {
}
