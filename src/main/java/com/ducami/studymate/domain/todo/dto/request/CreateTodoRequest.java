package com.ducami.studymate.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateTodoRequest(
        @NotBlank(message = "Todo 내용은 필수입니다.")
        String content
) {
}
