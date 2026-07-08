package com.ducami.studymate.domain.todo.dto.request;

import com.ducami.studymate.domain.todo.enums.TodoStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UpdateTodoStatusRequest {
    @NotNull(message = "상태는 필수입니다.")
    private TodoStatus status;
}
