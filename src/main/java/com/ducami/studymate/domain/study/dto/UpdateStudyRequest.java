package com.ducami.studymate.domain.study.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateStudyRequest {
    private String title;
    private String content;
}
