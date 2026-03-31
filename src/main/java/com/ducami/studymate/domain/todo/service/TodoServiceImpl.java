package com.ducami.studymate.domain.todo.service;

import com.ducami.studymate.domain.study.entity.StudyEntity;
import com.ducami.studymate.domain.study.exception.StudyNotFoundException;
import com.ducami.studymate.domain.study.repository.StudyRepository;
import com.ducami.studymate.domain.todo.dto.request.CreateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoStatusRequest;
import com.ducami.studymate.domain.todo.dto.response.TodoResponse;
import com.ducami.studymate.domain.todo.entity.TodoEntity;
import com.ducami.studymate.domain.todo.exception.TodoNotFoundException;
import com.ducami.studymate.domain.todo.repository.TodoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoServiceImpl implements TodoService {
    private final TodoRepository todoRepository;
    private final StudyRepository studyRepository;

    @Override
    public List<TodoResponse> findAll(Long studyId) {
        getStudy(studyId);

        return todoRepository.findAllByStudyIdOrderByIdAsc(studyId).stream()
                .map(TodoResponse::from)
                .toList();
    }

    @Override
    public TodoResponse findById(Long studyId, Long todoId) {
        return TodoResponse.from(getTodo(studyId, todoId));
    }

    @Override
    @Transactional
    public Long save(Long studyId, CreateTodoRequest request) {
        StudyEntity study = getStudy(studyId);
        return todoRepository.save(
                new TodoEntity(study, request)
        ).getId();
    }

    @Override
    @Transactional
    public void update(Long studyId, Long todoId, UpdateTodoRequest request) {
        getTodo(studyId, todoId).update(request);
    }

    @Override
    @Transactional
    public void updateStatus(Long studyId, Long todoId, UpdateTodoStatusRequest request) {
        TodoEntity todo = getTodo(studyId, todoId);
        todo.updateStatus(request.status());
    }

    @Override
    @Transactional
    public void delete(Long studyId, Long todoId) {
        todoRepository.delete(getTodo(studyId, todoId));
    }

    private StudyEntity getStudy(Long studyId) {
        return studyRepository.findById(studyId)
                .orElseThrow(StudyNotFoundException::new);
    }

    private TodoEntity getTodo(Long studyId, Long todoId) {
        getStudy(studyId);

        return todoRepository.findByIdAndStudyId(todoId, studyId)
                .orElseThrow(TodoNotFoundException::new);
    }
}
