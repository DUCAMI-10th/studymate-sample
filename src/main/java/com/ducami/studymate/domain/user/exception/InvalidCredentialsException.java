package com.ducami.studymate.domain.user.exception;

import com.ducami.studymate.domain.user.exception.status.UserStatusCode;
import com.ducami.studymate.global.exception.ApplicationException;

public class InvalidCredentialsException extends ApplicationException {
    public InvalidCredentialsException() {
        super(UserStatusCode.INVALID_CREDENTIALS);
    }
}
