package com.ducami.studymate.domain.user.exception;

import com.ducami.studymate.domain.user.exception.status.UserStatusCode;
import com.ducami.studymate.global.exception.ApplicationException;

public class UserNotFoundException extends ApplicationException {
    public UserNotFoundException() {
        super(UserStatusCode.USER_NOT_FOUND);
    }
}
