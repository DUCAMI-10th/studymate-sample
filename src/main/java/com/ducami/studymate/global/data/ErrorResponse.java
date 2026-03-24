package com.ducami.studymate.global.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ducami.studymate.global.exception.ErrorCode;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String code,
        int status,
        String message,
        LocalDateTime timestamp,
        Map<String, String> details
) {
    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getHttpStatus().value(),
                errorCode.getMessage(),
                LocalDateTime.now(),
                null
        );
    }

    public static ErrorResponse of(ErrorCode errorCode, String message) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getHttpStatus().value(),
                message,
                LocalDateTime.now(),
                null
        );
    }

    public static ErrorResponse of(ErrorCode errorCode, Map<String, String> details) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getHttpStatus().value(),
                errorCode.getMessage(),
                LocalDateTime.now(),
                details
        );
    }

    public static ErrorResponse of(ErrorCode errorCode, String message, Map<String, String> details) {
        return new ErrorResponse(
                errorCode.getCode(),
                errorCode.getHttpStatus().value(),
                message,
                LocalDateTime.now(),
                details
        );
    }
}
