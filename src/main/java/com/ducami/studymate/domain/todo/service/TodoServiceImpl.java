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
import com.ducami.studymate.domain.user.entity.UserEntity;
import com.ducami.studymate.domain.user.repository.UserRepository;
import com.ducami.studymate.global.exception.AuthenticatedUserNotFoundException;
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
    private final UserRepository userRepository;

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
    public Long save(Long studyId, CreateTodoRequest request, Long currentUserId) {
        StudyEntity study = getStudy(studyId);
        UserEntity author = getCurrentUser(currentUserId);
        return todoRepository.save(
                new TodoEntity(study, author, request)
        ).getId();
    }

    @Override
    @Transactional
    public void update(Long studyId, Long todoId, UpdateTodoRequest request, Long currentUserId) {
        TodoEntity todo = getTodo(studyId, todoId);
        todo.validateAuthor(currentUserId);
        todo.update(request);
    }

    @Override
    @Transactional
    public void updateStatus(Long studyId, Long todoId, UpdateTodoStatusRequest request, Long currentUserId) {
        TodoEntity todo = getTodo(studyId, todoId);
        todo.validateAuthor(currentUserId);
        todo.updateStatus(request.status());
    }

    @Override
    @Transactional
    public void delete(Long studyId, Long todoId, Long currentUserId) {
        TodoEntity todo = getTodo(studyId, todoId);
        todo.validateAuthor(currentUserId);
        todoRepository.delete(todo);
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

    private UserEntity getCurrentUser(Long currentUserId) {
        return userRepository.findById(currentUserId)
                .orElseThrow(AuthenticatedUserNotFoundException::new);
    }

}
