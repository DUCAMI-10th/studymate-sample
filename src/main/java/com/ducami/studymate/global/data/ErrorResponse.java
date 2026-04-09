package com.ducami.studymate.global.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ducami.studymate.global.exception.status.StatusCode;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        String code,
        String message,
        LocalDateTime timestamp,
        Map<String, String> details
) {
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
                .timestamp(LocalDateTime.now())
                .details(details)
                .build();
    }
}
