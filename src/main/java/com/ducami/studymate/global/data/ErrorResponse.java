package com.ducami.studymate.global.data;

import com.ducami.studymate.global.exception.status.StatusCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Builder
public class ErrorResponse {
    private String code;
    private String message;
    private Map<String, String> details;
    private LocalDateTime timestamp;

    public static ErrorResponse from(StatusCode statusCode) {
        return ErrorResponse.builder()
                .code(statusCode.getCode())
                .message(statusCode.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse from(StatusCode statusCode, Map<String, String> details) {
        return ErrorResponse.builder()
                .code(statusCode.getCode())
                .message(statusCode.getMessage())
                .details(details)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
