package com.ducami.studymate.global.exception.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum GlobalStatusCode implements StatusCode {
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "GLOBAL_400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "GLOBAL_401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "GLOBAL_403", "접근 권한이 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "GLOBAL_500", "서버 내부 오류가 발생했습니다."),;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
