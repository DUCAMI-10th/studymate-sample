package com.ducami.studymate.domain.todo.exception;

import com.ducami.studymate.global.exception.ApplicationException;

public class TodoNotFoundException extends ApplicationException {
    public TodoNotFoundException() {
        super(TodoStatusCode.TODO_NOT_FOUND);
    }

    public TodoNotFoundException(String message) {
        super(TodoStatusCode.TODO_NOT_FOUND, message);
    }
}
