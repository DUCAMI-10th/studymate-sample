package com.ducami.studymate.domain.study.controller;

import com.ducami.studymate.domain.study.dto.request.CreateStudyRequest;
import com.ducami.studymate.domain.study.dto.response.StudyResponse;
import com.ducami.studymate.domain.study.dto.response.StudySummaryResponse;
import com.ducami.studymate.domain.study.dto.request.UpdateStudyRequest;
import com.ducami.studymate.domain.study.service.StudyService;
import com.ducami.studymate.global.data.ApiResponse;
import com.ducami.studymate.global.security.principal.UserPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/studies")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<StudySummaryResponse>>> findAll() {
        return ApiResponse.ok(studyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<StudyResponse>> findById(@PathVariable Long id) {
        return ApiResponse.ok(studyService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> create(
            @RequestBody @Valid CreateStudyRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        studyService.save(request, userPrincipal.getUserId());
        return ApiResponse.created();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> update(
            @PathVariable Long id,
            @RequestBody @Valid UpdateStudyRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        studyService.update(id, request, userPrincipal.getUserId());
        return ApiResponse.ok();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        studyService.delete(id, userPrincipal.getUserId());
        return ApiResponse.ok();
    }
}
