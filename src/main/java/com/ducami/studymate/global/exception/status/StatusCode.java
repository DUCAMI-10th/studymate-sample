package com.ducami.studymate.global.exception.status;

import org.springframework.http.HttpStatus;

public interface StatusCode {
    HttpStatus getHttpStatus();
    String getCode();
    String getMessage();
}
