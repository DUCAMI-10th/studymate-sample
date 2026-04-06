package com.ducami.studymate.domain.todo.controller;

import com.ducami.studymate.domain.todo.dto.request.CreateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoStatusRequest;
import com.ducami.studymate.domain.todo.dto.response.TodoResponse;
import com.ducami.studymate.domain.todo.service.TodoService;
import com.ducami.studymate.global.data.ApiResponse;
import com.ducami.studymate.global.security.principal.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/studies/{studyId}/todos")
@RequiredArgsConstructor
public class TodoController {
    private final TodoService todoService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<TodoResponse>>> findAll(@PathVariable Long studyId) {
        return ApiResponse.ok(todoService.findAll(studyId));
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<ApiResponse<TodoResponse>> findById(
            @PathVariable Long studyId,
            @PathVariable Long todoId
    ) {
        return ApiResponse.ok(todoService.findById(studyId, todoId));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(
            @PathVariable Long studyId,
            @RequestBody @Valid CreateTodoRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        todoService.save(studyId, request, userPrincipal.getUserId());
        return ApiResponse.created();
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable Long studyId,
            @PathVariable Long todoId,
            @RequestBody @Valid UpdateTodoRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        todoService.update(studyId, todoId, request, userPrincipal.getUserId());
        return ApiResponse.ok();
    }

    @PatchMapping("/{todoId}/status")
    public ResponseEntity<ApiResponse<Void>> updateStatus(
            @PathVariable Long studyId,
            @PathVariable Long todoId,
            @RequestBody @Valid UpdateTodoStatusRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        todoService.updateStatus(studyId, todoId, request, userPrincipal.getUserId());
        return ApiResponse.ok();
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long studyId,
            @PathVariable Long todoId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        todoService.delete(studyId, todoId, userPrincipal.getUserId());
        return ApiResponse.ok();
    }
}
