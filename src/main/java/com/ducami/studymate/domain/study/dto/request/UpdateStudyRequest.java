package com.ducami.studymate.domain.study.dto.request;

import jakarta.validation.constraints.Pattern;

public record UpdateStudyRequest(
        @Pattern(regexp = ".*\\S.*", message = "제목은 비어 있을 수 없습니다.")
        String title,
        String content
) {
}
