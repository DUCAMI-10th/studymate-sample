package com.ducami.studymate.global.data;

import com.ducami.studymate.global.exception.status.StatusCode;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ErrorResponse {
    private String code;
    private String message;
    private Map<String, String> details;
    private LocalDateTime timestamp;

    public ErrorResponse(String code, String message, Map<String, String> details, LocalDateTime timestamp) {
        this.code = code;
        this.message = message;
        this.details = details;
        this.timestamp = timestamp;
    }

    public static ErrorResponse from(StatusCode statusCode) {
        return new ErrorResponse(
                statusCode.getCode(),
                statusCode.getMessage(),
                null,
                LocalDateTime.now()
        );
    }

    public static ErrorResponse from(StatusCode statusCode, Map<String, String> details) {
        return new ErrorResponse(
                statusCode.getCode(),
                statusCode.getMessage(),
                details,
                LocalDateTime.now()
        );
    }
}
