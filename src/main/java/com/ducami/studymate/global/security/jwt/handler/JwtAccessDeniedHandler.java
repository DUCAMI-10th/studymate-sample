package com.ducami.studymate.global.security.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ducami.studymate.global.data.ApiResponse;
import com.ducami.studymate.global.data.ErrorResponse;
import com.ducami.studymate.global.exception.status.GlobalStatusCode;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {
    private final ObjectMapper objectMapper;

    @Override
    public void handle(
            HttpServletRequest request,
            HttpServletResponse response,
            AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {
        GlobalStatusCode statusCode = GlobalStatusCode.FORBIDDEN;

        response.setStatus(statusCode.getHttpStatus().value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        ErrorResponse error = ErrorResponse.from(statusCode);
        ApiResponse<ErrorResponse> body = new ApiResponse<>(
                statusCode.getHttpStatus().value(),
                statusCode.getMessage(),
                error
        );
        objectMapper.writeValue(response.getWriter(), body);
    }
}
