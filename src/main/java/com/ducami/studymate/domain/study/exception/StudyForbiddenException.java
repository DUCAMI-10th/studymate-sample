package com.ducami.studymate.domain.study.exception;

import com.ducami.studymate.global.exception.ApplicationException;

public class StudyForbiddenException extends ApplicationException {
    public StudyForbiddenException() {
        super(StudyStatusCode.STUDY_FORBIDDEN);
    }

    public StudyForbiddenException(String message) {
        super(StudyStatusCode.STUDY_FORBIDDEN, message);
    }
}
