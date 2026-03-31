package com.ducami.studymate.domain.todo.dto.response;

import com.ducami.studymate.domain.todo.entity.TodoEntity;
import com.ducami.studymate.domain.todo.entity.TodoStatus;

public record TodoResponse(
        Long id,
        Long studyId,
        String content,
        TodoStatus status
) {
    public static TodoResponse from(TodoEntity entity) {
        return new TodoResponse(
                entity.getId(),
                entity.getStudy().getId(),
                entity.getContent(),
                entity.getStatus()
        );
    }
}
