package com.ducami.studymate.domain.auth.service;

import com.ducami.studymate.domain.auth.dto.request.LoginRequest;
import com.ducami.studymate.domain.user.dto.response.UserResponse;
import com.ducami.studymate.domain.user.entity.UserEntity;
import com.ducami.studymate.domain.user.exception.InvalidCredentialsException;
import com.ducami.studymate.domain.user.repository.UserRepository;
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

    @Override
    public UserResponse login(LoginRequest request) {
        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return UserResponse.toEntity(user);
    }
}
