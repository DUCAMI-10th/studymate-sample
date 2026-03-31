package com.ducami.studymate.domain.todo.exception;

import com.ducami.studymate.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum TodoStatusCode implements ErrorCode {
    TODO_NOT_FOUND(HttpStatus.NOT_FOUND, "TODO_404", "Todo를 찾을 수 없습니다."),
    TODO_FORBIDDEN(HttpStatus.FORBIDDEN, "TODO_403", "Todo 작성자만 수정/삭제/상태변경할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
