package com.ducami.studymate.domain.study.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CreateStudyRequest(
        @NotBlank(message = "제목은 필수입니다.")
        String title,
        String content
) {
}
