package com.ducami.studymate.domain.user.controller;

import com.ducami.studymate.domain.user.dto.request.SignupRequest;
import com.ducami.studymate.domain.user.dto.response.SignupResponse;
import com.ducami.studymate.domain.user.dto.response.UserResponse;
import com.ducami.studymate.domain.user.service.UserService;
import com.ducami.studymate.global.data.ApiResponse;
import com.ducami.studymate.global.security.principal.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getInfo(
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        return ApiResponse.ok("내 정보를 조회했습니다.", userService.getUser(userPrincipal.getUserId()));
    }

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignupResponse>> signup(@RequestBody @Valid SignupRequest request) {
        return ApiResponse.created("회원가입에 성공했습니다.", userService.signup(request));
    }
}
