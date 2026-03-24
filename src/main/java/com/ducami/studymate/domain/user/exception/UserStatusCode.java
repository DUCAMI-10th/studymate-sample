package com.ducami.studymate.domain.user.exception;

import com.ducami.studymate.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserStatusCode implements ErrorCode {
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "USER_409", "이미 사용 중인 이메일입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
