package com.ducami.studymate.domain.todo.controller;

import com.ducami.studymate.domain.todo.dto.request.CreateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoRequest;
import com.ducami.studymate.domain.todo.dto.request.UpdateTodoStatusRequest;
import com.ducami.studymate.domain.todo.dto.response.TodoResponse;
import com.ducami.studymate.domain.todo.service.TodoService;
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
    public ResponseEntity<List<TodoResponse>> findAll(@PathVariable Long studyId) {
        return ResponseEntity.ok(todoService.findAll(studyId));
    }

    @GetMapping("/{todoId}")
    public ResponseEntity<TodoResponse> findById(
            @PathVariable Long studyId,
            @PathVariable Long todoId
    ) {
        return ResponseEntity.ok(todoService.findById(studyId, todoId));
    }

    @PostMapping
    public ResponseEntity<Void> create(
            @PathVariable Long studyId,
            @RequestBody @Valid CreateTodoRequest request
    ) {
        todoService.save(studyId, request);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{todoId}")
    public ResponseEntity<Void> update(
            @PathVariable Long studyId,
            @PathVariable Long todoId,
            @RequestBody @Valid UpdateTodoRequest request
    ) {
        todoService.update(studyId, todoId, request);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{todoId}/status")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long studyId,
            @PathVariable Long todoId,
            @RequestBody @Valid UpdateTodoStatusRequest request
    ) {
        todoService.updateStatus(studyId, todoId, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{todoId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long studyId,
            @PathVariable Long todoId
    ) {
        todoService.delete(studyId, todoId);
        return ResponseEntity.noContent().build();
    }
}
