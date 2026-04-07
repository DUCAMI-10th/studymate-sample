package com.ducami.studymate.domain.user.service;

import com.ducami.studymate.domain.user.dto.request.SignupRequest;
import com.ducami.studymate.domain.user.dto.response.SignupResponse;
import com.ducami.studymate.domain.user.dto.response.UserResponse;
import com.ducami.studymate.domain.user.entity.UserEntity;
import com.ducami.studymate.domain.user.exception.EmailAlreadyExistsException;
import com.ducami.studymate.domain.user.exception.UserNotFoundException;
import com.ducami.studymate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SignupResponse signup(SignupRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException();
        }

        UserEntity user = UserEntity.signup(request, passwordEncoder.encode(request.password()));
        return SignupResponse.from(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse getUser(Long userId) {
        return UserResponse.from(
                userRepository.findById(userId)
                    .orElseThrow(UserNotFoundException::new)
        );
    }
}
