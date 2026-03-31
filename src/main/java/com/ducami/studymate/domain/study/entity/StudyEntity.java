package com.ducami.studymate.domain.study.entity;

import com.ducami.studymate.domain.study.dto.request.CreateStudyRequest;
import com.ducami.studymate.domain.study.dto.request.UpdateStudyRequest;
import com.ducami.studymate.domain.study.exception.StudyForbiddenException;
import com.ducami.studymate.domain.todo.entity.TodoEntity;
import com.ducami.studymate.domain.user.entity.UserEntity;
import com.ducami.studymate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity
@Table(name = "tb_studies")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    @Builder.Default
    @OneToMany(mappedBy = "study", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TodoEntity> todos = new ArrayList<>();

    public StudyEntity(CreateStudyRequest request, UserEntity owner) {
        this.title = request.title();
        this.content = request.content();
        this.owner = owner;
    }
    
    public void update(UpdateStudyRequest request) {
        if (request.title() != null) this.title = request.title();
        if (request.content() != null) this.content = request.content();
    }

    public void validateOwner(Long ownerId) {
        if (!this.getOwner().getId().equals(ownerId)) {
            throw new StudyForbiddenException();
        }
    }
}
