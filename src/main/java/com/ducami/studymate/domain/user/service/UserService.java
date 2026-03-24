package com.ducami.studymate.domain.user.service;

import com.ducami.studymate.domain.user.dto.request.SignupRequest;
import com.ducami.studymate.domain.user.dto.response.SignupResponse;

public interface UserService {
    SignupResponse signup(SignupRequest request);
}
