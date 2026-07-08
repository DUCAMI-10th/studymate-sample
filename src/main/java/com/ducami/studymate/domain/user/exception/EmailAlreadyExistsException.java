package com.ducami.studymate.domain.user.exception;

import com.ducami.studymate.domain.user.exception.status.UserStatusCode;
import com.ducami.studymate.global.exception.ApplicationException;

public class EmailAlreadyExistsException extends ApplicationException {
    public EmailAlreadyExistsException() {
        super(UserStatusCode.EMAIL_ALREADY_EXISTS);
    }
}
