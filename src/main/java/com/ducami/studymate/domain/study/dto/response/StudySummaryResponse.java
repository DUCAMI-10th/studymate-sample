package com.ducami.studymate.domain.study.dto.response;

import com.ducami.studymate.domain.study.entity.StudyEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudySummaryResponse {
    private Long id;
    private String title;

    public static StudySummaryResponse toEntity(StudyEntity entity) {
        return new StudySummaryResponse(
                entity.getId(),
                entity.getTitle()
        );
    }
}
