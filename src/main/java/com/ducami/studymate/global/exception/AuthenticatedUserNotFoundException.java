package com.ducami.studymate.global.exception;

import com.ducami.studymate.global.exception.status.GlobalStatusCode;

public class AuthenticatedUserNotFoundException extends ApplicationException {
    public AuthenticatedUserNotFoundException() {
        super(GlobalStatusCode.UNAUTHORIZED, "인증된 사용자를 찾을 수 없습니다.");
    }
}
