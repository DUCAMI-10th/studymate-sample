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

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse login(LoginRequest request) {
        Optional<UserEntity> userOptional = userRepository.findByEmail(request.getEmail());

        if (userOptional.isEmpty()) {
            throw new InvalidCredentialsException();
        }

        UserEntity user = userOptional.get();

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        return UserResponse.from(user);
    }
}
