package com.ducami.studymate.global.exception;

import com.ducami.studymate.global.exception.BusinessException;
import com.ducami.studymate.global.exception.GlobalStatusCode;

public class ExpiredTokenException extends BusinessException {
    public ExpiredTokenException() {
        super(GlobalStatusCode.UNAUTHORIZED, "만료된 토큰입니다.");
    }
}
