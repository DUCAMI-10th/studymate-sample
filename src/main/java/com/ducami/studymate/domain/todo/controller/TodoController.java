package com.ducami.studymate.domain.todo.controller;

import com.ducami.studymate.domain.todo.dto.request.CreateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoStatusRequest;
import com.ducami.studymate.domain.todo.dto.response.TodoResponse;
import com.ducami.studymate.domain.todo.service.TodoService;
import com.ducami.studymate.global.data.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/studies/{studyId}/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoResponse>>> findAll(@PathVariable Long studyId) {
        return ApiResponse.success(todoService.findAll(studyId));
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<ApiResponse<TodoResponse>> findById(
            @PathVariable Long studyId,
            @PathVariable Long todoId
    ) {
        return ApiResponse.success(todoService.findById(studyId, todoId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(
            @PathVariable Long studyId,
            @RequestBody @Valid CreateTodoRequest request
    ) {
        todoService.save(studyId, request);
        return ApiResponse.created("Todo를 등록했습니다.");
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable Long studyId,
            @PathVariable Long todoId,
            @RequestBody @Valid UpdateTodoRequest request
    ) {
        todoService.update(studyId, todoId, request);
        return ApiResponse.success("Todo를 수정했습니다.");
    }

    @PatchMapping("/{todoId}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable Long studyId,
            @PathVariable Long todoId,
            @RequestBody @Valid UpdateTodoStatusRequest request
    ) {
        todoService.updateStatus(studyId, todoId, request);
        return ApiResponse.success("Todo 상태를 변경했습니다.");
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long studyId,
            @PathVariable Long todoId
    ) {
        todoService.delete(studyId, todoId);
        return ApiResponse.success("Todo를 삭제했습니다.");
    }
}
