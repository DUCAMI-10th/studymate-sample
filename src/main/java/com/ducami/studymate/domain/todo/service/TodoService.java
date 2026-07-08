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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TodoService {
    private final TodoRepository todoRepository;
    private final StudyRepository studyRepository;

    public List<TodoResponse> findAll(Long studyId) {
        findStudyOrThrow(studyId);

        List<TodoEntity> todos = todoRepository.findAllByStudyIdOrderByIdAsc(studyId);
        List<TodoResponse> responses = new ArrayList<>();

        for (TodoEntity todo : todos) {
            TodoResponse response = TodoResponse.from(todo);
            responses.add(response);
        }

        return responses;
    }

    public TodoResponse findById(Long studyId, Long todoId) {
        return TodoResponse.from(findTodoOrThrow(studyId, todoId));
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
        Optional<StudyEntity> studyOptional = studyRepository.findById(studyId);

        if (studyOptional.isEmpty()) {
            throw new StudyNotFoundException();
        }

        return studyOptional.get();
    }

    private TodoEntity findTodoOrThrow(Long studyId, Long todoId) {
        findStudyOrThrow(studyId);

        Optional<TodoEntity> todoOptional = todoRepository.findByIdAndStudyId(todoId, studyId);

        if (todoOptional.isEmpty()) {
            throw new TodoNotFoundException();
        }

        return todoOptional.get();
    }
}
