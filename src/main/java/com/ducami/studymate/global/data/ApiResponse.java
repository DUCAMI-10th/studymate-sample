package com.ducami.studymate.global.data;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.ducami.studymate.global.exception.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private final T data;
    private final int status;
    private final String message;

    private ApiResponse(T data, int status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }

    private static <T> ResponseEntity<ApiResponse<T>> response(HttpStatus httpStatus, T data, String message) {
        return ResponseEntity.status(httpStatus)
                .body(new ApiResponse<>(data, httpStatus.value(), message));
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return response(HttpStatus.OK, data, null);
    }

    public static ResponseEntity<ApiResponse<Void>> success() {
        return response(HttpStatus.OK, null, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return response(HttpStatus.OK, data, message);
    }

    public static ResponseEntity<ApiResponse<Void>> success(String message) {
        return response(HttpStatus.OK, null, message);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return response(HttpStatus.CREATED, data, null);
    }

    public static ResponseEntity<ApiResponse<Void>> created() {
        return response(HttpStatus.CREATED, null, null);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return response(HttpStatus.CREATED, data, message);
    }

    public static ResponseEntity<ApiResponse<Void>> created(String message) {
        return response(HttpStatus.CREATED, null, message);
    }

    public static ResponseEntity<ApiResponse<ErrorResponse>> error(ErrorCode errorCode) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode);
        return response(errorCode.getHttpStatus(), errorResponse, errorResponse.getMessage());
    }

    public static ResponseEntity<ApiResponse<ErrorResponse>> error(ErrorCode errorCode, String message) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message);
        return response(errorCode.getHttpStatus(), errorResponse, errorResponse.getMessage());
    }

    public static ResponseEntity<ApiResponse<ErrorResponse>> error(ErrorCode errorCode, Map<String, String> details) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, details);
        return response(errorCode.getHttpStatus(), errorResponse, errorResponse.getMessage());
    }

    public static ResponseEntity<ApiResponse<ErrorResponse>> error(
            ErrorCode errorCode,
            String message,
            Map<String, String> details
    ) {
        ErrorResponse errorResponse = ErrorResponse.of(errorCode, message, details);
        return response(errorCode.getHttpStatus(), errorResponse, errorResponse.getMessage());
    }

}
