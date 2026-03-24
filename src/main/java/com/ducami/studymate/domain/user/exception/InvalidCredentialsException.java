package com.ducami.studymate.domain.user.exception;

import com.ducami.studymate.global.exception.BusinessException;

public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException() {
        super(UserStatusCode.INVALID_CREDENTIALS);
    }
}
