package com.ducami.studymate.domain.auth.service;

import com.ducami.studymate.domain.auth.dto.request.LoginRequest;
import com.ducami.studymate.domain.auth.dto.response.TokenResponse;

public interface AuthService {
    TokenResponse login(LoginRequest request);
}
