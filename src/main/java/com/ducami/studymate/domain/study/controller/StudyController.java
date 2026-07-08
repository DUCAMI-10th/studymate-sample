package com.ducami.studymate.domain.study.controller;

import com.ducami.studymate.domain.study.dto.StudyResponse;
import com.ducami.studymate.domain.study.dto.StudySummaryResponse;
import com.ducami.studymate.domain.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/studies")
@RequiredArgsConstructor
public class StudyController {
    private final StudyService studyService;

    @GetMapping
    public ResponseEntity<List<StudySummaryResponse>> findAll() {
        return ResponseEntity.ok(studyService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(studyService.findById(id));
    }
}
