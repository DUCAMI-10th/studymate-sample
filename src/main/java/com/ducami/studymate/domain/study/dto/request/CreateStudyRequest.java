package com.ducami.studymate.domain.study.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateStudyRequest {
    private String title;
    private String content;
}
