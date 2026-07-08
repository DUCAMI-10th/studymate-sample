package com.ducami.studymate.domain.auth.service;

import com.ducami.studymate.domain.auth.dto.request.LoginRequest;
import com.ducami.studymate.domain.user.dto.response.UserResponse;

public interface AuthService {
    UserResponse login(LoginRequest request);
}
