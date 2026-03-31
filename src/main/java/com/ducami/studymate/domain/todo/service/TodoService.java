package com.ducami.studymate.domain.todo.service;

import com.ducami.studymate.domain.todo.dto.request.CreateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoStatusRequest;
import com.ducami.studymate.domain.todo.dto.response.TodoResponse;

import java.util.List;

public interface TodoService {

    List<TodoResponse> findAll(Long studyId);

    TodoResponse findById(Long studyId, Long todoId);

    Long save(Long studyId, CreateTodoRequest request, Long currentUserId);

    void update(Long studyId, Long todoId, UpdateTodoRequest request, Long currentUserId);

    void updateStatus(Long studyId, Long todoId, UpdateTodoStatusRequest request, Long currentUserId);

    void delete(Long studyId, Long todoId, Long currentUserId);
}
