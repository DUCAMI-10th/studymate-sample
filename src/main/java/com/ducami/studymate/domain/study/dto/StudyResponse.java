package com.ducami.studymate.domain.study.dto;

import com.ducami.studymate.domain.study.entity.StudyEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudyResponse {
    private Long id;
    private String title;
    private String content;

    public static StudyResponse toEntity(StudyEntity entity) {
        return new StudyResponse(
                entity.getId(),
                entity.getTitle(),
                entity.getContent()
        );
    }
}
