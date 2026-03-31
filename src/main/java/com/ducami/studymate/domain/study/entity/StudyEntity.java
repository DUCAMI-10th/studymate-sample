package com.ducami.studymate.domain.study.entity;

import com.ducami.studymate.domain.study.dto.request.CreateStudyRequest;
import com.ducami.studymate.domain.study.dto.request.UpdateStudyRequest;
import com.ducami.studymate.domain.todo.entity.TodoEntity;
import com.ducami.studymate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Builder.Default
    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoEntity> todos = new ArrayList<>();

    public StudyEntity(CreateStudyRequest request) {
        this.title = request.title();
        this.content = request.content();
    }

    public void update(UpdateStudyRequest request) {
        if (request.title() != null) this.title = request.title();
        if (request.content() != null) this.content = request.content();
    }
}
