package com.ducami.studymate.domain.todo.service;

import com.ducami.studymate.domain.study.entity.StudyEntity;
import com.ducami.studymate.domain.study.repository.StudyRepository;
import com.ducami.studymate.domain.todo.dto.request.CreateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoStatusRequest;
import com.ducami.studymate.domain.todo.dto.response.TodoResponse;
import com.ducami.studymate.domain.todo.entity.TodoEntity;
import com.ducami.studymate.domain.todo.exception.TodoNotFoundException;
import com.ducami.studymate.domain.todo.repository.TodoRepository;
import com.ducami.studymate.domain.study.exception.StudyNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {
    private final TodoRepository todoRepository;
    private final StudyRepository studyRepository;
    public List<TodoResponse> findAll(Long studyId) {
        findStudyOrThrow(studyId);

        return todoRepository.findAllByStudyIdOrderByIdAsc(studyId).stream()
                .map(TodoResponse::toEntity)
                .toList();
    }
    public TodoResponse findById(Long studyId, Long todoId) {
        return TodoResponse.toEntity(findTodoOrThrow(studyId, todoId));
    }
    @Transactional
    public Long save(Long studyId, CreateTodoRequest request) {
        StudyEntity study = findStudyOrThrow(studyId);
        return todoRepository.save(new TodoEntity(study, request)).getId();
    }
    @Transactional
    public void update(Long studyId, Long todoId, UpdateTodoRequest request) {
        TodoEntity todo = findTodoOrThrow(studyId, todoId);
        todo.update(request);
    }
    @Transactional
    public void updateStatus(Long studyId, Long todoId, UpdateTodoStatusRequest request) {
        TodoEntity todo = findTodoOrThrow(studyId, todoId);
        todo.updateStatus(request.getStatus());
    }
    @Transactional
    public void delete(Long studyId, Long todoId) {
        TodoEntity todo = findTodoOrThrow(studyId, todoId);
        todoRepository.delete(todo);
    }

    private StudyEntity findStudyOrThrow(Long studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(StudyNotFoundException::new);
    }

    private TodoEntity findTodoOrThrow(Long studyId, Long todoId) {
        findStudyOrThrow(studyId);

        return todoRepository.findByIdAndStudyId(todoId, studyId)
                .orElseThrow(TodoNotFoundException::new);
    }
}
