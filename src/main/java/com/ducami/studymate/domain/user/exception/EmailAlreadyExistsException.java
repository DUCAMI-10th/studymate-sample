package com.ducami.studymate.domain.user.exception;

import com.ducami.studymate.global.exception.BusinessException;

public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException() {
        super(UserStatusCode.EMAIL_ALREADY_EXISTS);
    }
}
