package com.ducami.studymate.global.data;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

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

    public static <T> ResponseEntity<ApiResponse<T>> success(T data) {
        return ResponseEntity.ok(
                new ApiResponse<>(data, 200, null)
        );
    }

    public static ResponseEntity<ApiResponse<Void>> success() {
        return ResponseEntity.ok(
                new ApiResponse<>(null, 200, null)
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message) {
        return ResponseEntity.ok(
                new ApiResponse<>(data, 200, message)
        );
    }

    public static ResponseEntity<ApiResponse<Void>> success(String message) {
        return ResponseEntity.ok(
                new ApiResponse<>(null, 200, message)
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data) {
        return ResponseEntity.status(201).body(
                new ApiResponse<>(data, 201, null)
        );
    }

    public static ResponseEntity<ApiResponse<Void>> created() {
        return ResponseEntity.status(201).body(
                new ApiResponse<>(null, 201, null)
        );
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return ResponseEntity.status(201).body(
                new ApiResponse<>(data, 201, message)
        );
    }

    public static ResponseEntity<ApiResponse<Void>> created(String message) {
        return ResponseEntity.status(201).body(
                new ApiResponse<>(null, 201, message)
        );
    }

}
