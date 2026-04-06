package com.ducami.studymate.global.exception;

import com.ducami.studymate.global.exception.status.GlobalStatusCode;

public class ExpiredTokenException extends ApplicationException {
    public ExpiredTokenException() {
        super(GlobalStatusCode.UNAUTHORIZED, "만료된 토큰입니다.");
    }
}
