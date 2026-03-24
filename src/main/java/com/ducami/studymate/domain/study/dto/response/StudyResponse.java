package com.ducami.studymate.domain.study.dto.response;

import com.ducami.studymate.domain.study.entity.StudyEntity;

public record StudyResponse(
        Long id,
        String title,
        String content
) {
    public static StudyResponse toEntity(StudyEntity entity) {
        return new StudyResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getContent()
        );
    }
}
