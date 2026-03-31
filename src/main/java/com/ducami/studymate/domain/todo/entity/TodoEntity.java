package com.ducami.studymate.domain.todo.entity;

import com.ducami.studymate.domain.study.entity.StudyEntity;
import com.ducami.studymate.domain.todo.dto.request.CreateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoRequest;
import com.ducami.studymate.domain.user.entity.UserEntity;
import com.ducami.studymate.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@Entity(name = "tb_todoes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TodoEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "study_id", nullable = false)
    private StudyEntity study;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TodoStatus status;

    public TodoEntity(StudyEntity study, CreateTodoRequest request) {
        this.study = study;
        this.content = request.content();
        this.status = TodoStatus.PENDING;
    }

    public void update(UpdateTodoRequest request) {
        this.content = request.content();
    }

    public void updateStatus(TodoStatus status) {
        this.status = status;
    }
}
