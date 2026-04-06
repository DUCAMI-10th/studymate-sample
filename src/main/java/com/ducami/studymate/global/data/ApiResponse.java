package com.ducami.studymate.global.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ducami.studymate.global.exception.status.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiResponse<T>(
        boolean success,
        T data,
        ErrorResponse error
) {
    public ApiResponse(T data) {
        this(true, data, null);
    }

    public ApiResponse(ErrorResponse error) {
        this(false, null, error);
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok() {
        return ResponseEntity.ok(new ApiResponse<>(null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data) {
        return ResponseEntity.ok(new ApiResponse<>(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created() {
        return ResponseEntity.status(201)
                .body(new ApiResponse<>(null));
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(201)
                .body(new ApiResponse<>(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> of(int status, T data) {
        return ResponseEntity.status(status)
                .body(new ApiResponse<>(data));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(ErrorResponse error) {
        return ResponseEntity.status(error.status())
                .body(new ApiResponse<>(error));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(int status, String code, String message) {
        return error(ErrorResponse.of(status, code, message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> error(int status, String code, String message, Map<String, String> details) {
        return error(ErrorResponse.of(status, code, message, details));
    }
}
