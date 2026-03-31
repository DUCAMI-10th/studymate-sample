package com.ducami.studymate.domain.todo.service;

import com.ducami.studymate.domain.todo.dto.request.CreateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoStatusRequest;
import com.ducami.studymate.domain.todo.dto.response.TodoResponse;

import java.util.List;

public interface TodoService {

    List<TodoResponse> findAll(Long studyId);

    TodoResponse findById(Long studyId, Long todoId);

    Long save(Long studyId, CreateTodoRequest request);

    void update(Long studyId, Long todoId, UpdateTodoRequest request);

    void updateStatus(Long studyId, Long todoId, UpdateTodoStatusRequest request);

    void delete(Long studyId, Long todoId);
}
