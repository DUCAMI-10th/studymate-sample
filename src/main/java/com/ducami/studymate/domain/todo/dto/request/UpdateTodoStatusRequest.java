package com.ducami.studymate.domain.todo.dto.request;

import com.ducami.studymate.domain.todo.enums.TodoStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateTodoStatusRequest(
        @NotNull(message = "Todo 상태는 필수입니다.")
        TodoStatus status
) {
}
