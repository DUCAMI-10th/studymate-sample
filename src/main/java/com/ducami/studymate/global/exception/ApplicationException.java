package com.ducami.studymate.global.exception;

import com.ducami.studymate.global.exception.status.StatusCode;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private final StatusCode statusCode;

    public ApplicationException(StatusCode statusCode) {
        super(statusCode.getMessage());
        this.statusCode = statusCode;
    }

    public ApplicationException(StatusCode statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }
}
