package com.ducami.studymate.global.data;

import com.ducami.studymate.global.exception.status.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class ApiResponse<T> {
    private final int status;
    private final String message;
    private final T data;

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(String message, T data) {
        return of(HttpStatus.OK, message, data);
    }

    public static ResponseEntity<ApiResponse<Void>> ok(String message) {
        return of(HttpStatus.OK, message, null);
    }

    public static ResponseEntity<ApiResponse<Void>> created(String message) {
        return of(HttpStatus.CREATED, message, null);
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
