package com.ducami.studymate.domain.auth.service;

import com.ducami.studymate.domain.auth.dto.request.LoginRequest;
import com.ducami.studymate.domain.auth.dto.response.TokenResponse;
import com.ducami.studymate.domain.user.entity.UserEntity;
import com.ducami.studymate.domain.user.exception.InvalidCredentialsException;
import com.ducami.studymate.domain.user.repository.UserRepository;
import com.ducami.studymate.global.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public TokenResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return TokenResponse.from(jwtProvider.generateToken(user));
    }
}
