package com.ducami.studymate.global.exception.handler;

import com.ducami.studymate.global.data.ApiResponse;
import com.ducami.studymate.global.data.ErrorResponse;
import com.ducami.studymate.global.exception.ApplicationException;
import com.ducami.studymate.global.exception.status.GlobalStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleApplicationException(ApplicationException e) {
        return ApiResponse.error(e.getStatusCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> details = new LinkedHashMap<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            details.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        return ApiResponse.error(GlobalStatusCode.INVALID_INPUT_VALUE, details);
    }
}
