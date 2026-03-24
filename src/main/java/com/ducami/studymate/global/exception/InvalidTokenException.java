package com.ducami.studymate.global.exception;

import com.ducami.studymate.global.exception.BusinessException;
import com.ducami.studymate.global.exception.GlobalStatusCode;

public class InvalidTokenException extends BusinessException {
    public InvalidTokenException() {
        super(GlobalStatusCode.UNAUTHORIZED, "유효하지 않은 토큰입니다.");
    }

    public InvalidTokenException(String message) {
        super(GlobalStatusCode.UNAUTHORIZED, message);
    }
}
