package com.ducami.studymate.domain.user.service;

import com.ducami.studymate.domain.user.dto.request.SignupRequest;
import com.ducami.studymate.domain.user.dto.response.SignupResponse;
import com.ducami.studymate.domain.user.dto.response.UserResponse;

public interface UserService {
    SignupResponse signup(SignupRequest request);

    UserResponse getUser(Long userId);
}
