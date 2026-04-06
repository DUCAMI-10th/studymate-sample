package com.ducami.studymate.domain.study.exception;

import com.ducami.studymate.global.exception.ApplicationException;

public class StudyNotFoundException extends ApplicationException {
    public StudyNotFoundException() {
        super(StudyStatusCode.STUDY_NOT_FOUND);
    }

    public StudyNotFoundException(String message) {
        super(StudyStatusCode.STUDY_NOT_FOUND, message);
    }
}
