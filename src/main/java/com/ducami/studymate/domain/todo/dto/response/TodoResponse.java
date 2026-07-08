package com.ducami.studymate.domain.todo.dto.response;

import com.ducami.studymate.domain.todo.entity.TodoEntity;
import com.ducami.studymate.domain.todo.enums.TodoStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TodoResponse {
    private Long id;
    private String content;
    private TodoStatus status;

    public static TodoResponse from(TodoEntity entity) {
        return new TodoResponse(
                entity.getId(),
                entity.getContent(),
                entity.getStatus()
        );
    }
}
