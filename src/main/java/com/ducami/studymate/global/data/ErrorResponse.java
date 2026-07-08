package com.ducami.studymate.global.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ducami.studymate.global.exception.status.StatusCode;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String code,
        String message,
        LocalDateTime timestamp,
        Map<String, String> details
) {
    public static ErrorResponse from(StatusCode statusCode) {
        return new ErrorResponse(
                statusCode.getCode(),
                statusCode.getMessage(),
                LocalDateTime.now(),
                null
        );
    }

    public static ErrorResponse from(StatusCode statusCode, Map<String, String> details) {
        return new ErrorResponse(
                statusCode.getCode(),
                statusCode.getMessage(),
                LocalDateTime.now(),
                details
        );
    }
}
