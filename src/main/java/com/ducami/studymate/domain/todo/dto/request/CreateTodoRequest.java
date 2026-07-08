package com.ducami.studymate.domain.todo.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CreateTodoRequest {
    @NotBlank(message = "내용은 필수입니다.")
    private String content;
}
