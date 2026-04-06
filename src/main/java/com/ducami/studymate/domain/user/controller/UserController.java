package com.ducami.studymate.domain.user.controller;

import com.ducami.studymate.domain.user.dto.request.SignupRequest;
import com.ducami.studymate.domain.user.dto.response.SignupResponse;
import com.ducami.studymate.domain.user.service.UserService;
import com.ducami.studymate.global.data.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@RequestBody @Valid SignupRequest request) {
        return ApiResponse.created(userService.signup(request));
    }
}
