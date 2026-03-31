package com.ducami.studymate.domain.todo.repository;

import com.ducami.studymate.domain.todo.entity.TodoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TodoRepository extends JpaRepository<TodoEntity, Long> {
    List<TodoEntity> findAllByStudyIdOrderByIdAsc(Long studyId);

    Optional<TodoEntity> findByIdAndStudyId(Long todoId, Long studyId);
}
