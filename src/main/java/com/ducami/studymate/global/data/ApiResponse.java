package com.ducami.studymate.global.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ducami.studymate.global.exception.status.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        int status,
        String message,
        T data
) {
    public static ResponseEntity<ApiResponse<Void>> ok() {
        return ok("요청에 성공했습니다.");
    }

    public static ResponseEntity<ApiResponse<Void>> ok(String message) {
        return of(HttpStatus.OK, message, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ok("요청에 성공했습니다.", data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return of(HttpStatus.OK, message, data);
    }

    public static ResponseEntity<ApiResponse<Void>> created() {
        return created("생성에 성공했습니다.");
    }

    public static ResponseEntity<ApiResponse<Void>> created(String message) {
        return of(HttpStatus.CREATED, message, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return created("생성에 성공했습니다.", data);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(String message, T data) {
        return of(HttpStatus.CREATED, message, data);
    }

    public static ResponseEntity<ApiResponse<ErrorResponse>> error(StatusCode statusCode) {
        return error(statusCode, ErrorResponse.from(statusCode));
    }

    public static ResponseEntity<ApiResponse<ErrorResponse>> error(
            StatusCode statusCode,
            Map<String, String> details
    ) {
        return error(statusCode, ErrorResponse.from(statusCode, details));
    }

    public static ResponseEntity<ApiResponse<ErrorResponse>> error(
            StatusCode statusCode,
            ErrorResponse errorResponse
    ) {
        return of(statusCode.getHttpStatus(), statusCode.getMessage(), errorResponse);
    }

    private static <T> ResponseEntity<ApiResponse<T>> of(HttpStatus status, String message, T data) {
        return ResponseEntity.status(status)
                .body(new ApiResponse<>(status.value(), message, data));
    }
}
