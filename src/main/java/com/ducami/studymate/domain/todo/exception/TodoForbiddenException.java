package com.ducami.studymate.domain.todo.exception;

import com.ducami.studymate.global.exception.ApplicationException;

public class TodoForbiddenException extends ApplicationException {
    public TodoForbiddenException() {
        super(TodoStatusCode.TODO_FORBIDDEN);
    }

    public TodoForbiddenException(String message) {
        super(TodoStatusCode.TODO_FORBIDDEN, message);
    }
}
