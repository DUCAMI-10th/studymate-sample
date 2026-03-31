package com.ducami.studymate.domain.todo.exception;

import com.ducami.studymate.global.exception.BusinessException;

public class TodoNotFoundException extends BusinessException {
    public TodoNotFoundException() {
        super(TodoStatusCode.TODO_NOT_FOUND);
    }

    public TodoNotFoundException(String message) {
        super(TodoStatusCode.TODO_NOT_FOUND, message);
    }
}
