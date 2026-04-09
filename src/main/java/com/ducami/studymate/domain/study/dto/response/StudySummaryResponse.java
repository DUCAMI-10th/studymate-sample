package com.ducami.studymate.domain.study.dto.response;

import com.ducami.studymate.domain.study.entity.StudyEntity;

public record StudySummaryResponse(
        Long id,
        String title
) {
    public static StudySummaryResponse from(StudyEntity entity) {
        return new StudySummaryResponse(
                entity.getId(),
                entity.getTitle()
        );
    }
}
