package com.ducami.studymate.domain.study.entity;

import com.ducami.studymate.domain.study.dto.request.CreateStudyRequest;
import com.ducami.studymate.domain.study.dto.request.UpdateStudyRequest;
import com.ducami.studymate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity(name = "tb_studies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    public StudyEntity(CreateStudyRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
    }

    public void update(UpdateStudyRequest request) {
        if (request.getTitle() != null) this.title = request.getTitle();
        if (request.getContent() != null) this.content = request.getContent();
    }
}
