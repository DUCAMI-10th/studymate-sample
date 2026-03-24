package com.ducami.studymate.domain.user.exception;

import com.ducami.studymate.global.exception.BusinessException;

public class InvalidRefreshTokenException extends BusinessException {
    public InvalidRefreshTokenException() {
        super(UserStatusCode.INVALID_REFRESH_TOKEN);
    }
}
