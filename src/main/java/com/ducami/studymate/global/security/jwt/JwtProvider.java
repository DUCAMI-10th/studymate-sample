package com.ducami.studymate.global.security.jwt;

import com.ducami.studymate.domain.user.entity.UserEntity;
import com.ducami.studymate.global.security.jwt.config.JwtProperties;
import com.ducami.studymate.global.exception.ExpiredTokenException;
import com.ducami.studymate.global.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtProvider {
    private final JwtProperties jwtProperties;
    private final SecretKey key;

    public JwtProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.key = Keys.hmacShaKeyFor(jwtProperties.secret().getBytes(StandardCharsets.UTF_8));
    }

    public JwtToken generateToken(UserEntity user) {
        long now = System.currentTimeMillis();
        long accessTokenExpiresAt = now + jwtProperties.accessTokenExpiration();

        return new JwtToken(generateAccessToken(user, accessTokenExpiresAt));
    }

    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (ExpiredTokenException | InvalidTokenException e) {
            return false;
        }
    }

    public Long getUserId(String token) {
        return Long.parseLong(getClaims(token).getSubject());
    }

    public Claims getClaims(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (ExpiredJwtException e) {
            throw new ExpiredTokenException();
        } catch (MalformedJwtException e) {
            throw new InvalidTokenException("JWT 토큰 형식이 올바르지 않습니다.");
        } catch (JwtException | IllegalArgumentException e) {
            throw new InvalidTokenException();
        }
    }

    private String generateAccessToken(UserEntity user, long expiresAt) {
        Date now = new Date();
        Date expiration = new Date(expiresAt);

        return Jwts.builder()
                .issuer(jwtProperties.issuer())
                .subject(String.valueOf(user.getId()))
                .claim("authority", user.getRole().name())
                .claim("email", user.getEmail())
                .issuedAt(now)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }
}
