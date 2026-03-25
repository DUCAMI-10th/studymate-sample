package com.ducami.studymate.domain.auth.service;

import com.ducami.studymate.domain.auth.dto.request.LoginRequest;
import com.ducami.studymate.domain.auth.dto.request.TokenRefreshRequest;
import com.ducami.studymate.domain.auth.dto.response.TokenResponse;
import com.ducami.studymate.domain.user.entity.UserEntity;
import com.ducami.studymate.domain.user.exception.InvalidCredentialsException;
import com.ducami.studymate.domain.user.exception.InvalidRefreshTokenException;
import com.ducami.studymate.domain.user.repository.UserRepository;
import com.ducami.studymate.global.security.jwt.JwtProvider;
import com.ducami.studymate.global.security.jwt.JwtToken;
import com.ducami.studymate.global.security.jwt.enums.TokenType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Override
    @Transactional
    public TokenResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return issueTokens(user);
    }

    @Override
    @Transactional
    public TokenResponse refresh(TokenRefreshRequest request) {
        String refreshToken = request.refreshToken();

        if (!jwtProvider.validateToken(refreshToken) || !jwtProvider.isTokenType(refreshToken, TokenType.REFRESH)) {
            throw new InvalidRefreshTokenException();
        }

        UserEntity user = userRepository.findByEmail(jwtProvider.getEmail(refreshToken))
                .orElseThrow(InvalidRefreshTokenException::new);

        return issueTokens(user);
    }

    private TokenResponse issueTokens(UserEntity user) {
        JwtToken jwtToken = jwtProvider.generateTokens(user);
        return TokenResponse.from(jwtToken);
    }
}
