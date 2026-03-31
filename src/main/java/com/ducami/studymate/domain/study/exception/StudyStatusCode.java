package com.ducami.studymate.domain.study.exception;

import com.ducami.studymate.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum StudyStatusCode implements ErrorCode {
    STUDY_NOT_FOUND(HttpStatus.NOT_FOUND, "STUDY_404", "스터디를 찾을 수 없습니다."),
    STUDY_FORBIDDEN(HttpStatus.FORBIDDEN, "STUDY_403", "스터디 작성자만 수정/삭제할 수 있습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
