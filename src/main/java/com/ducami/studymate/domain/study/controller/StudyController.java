package com.ducami.studymate.domain.study.controller;

import com.ducami.studymate.domain.study.dto.CreateStudyRequest;
import com.ducami.studymate.domain.study.dto.StudyResponse;
import com.ducami.studymate.domain.study.dto.StudySummaryResponse;
import com.ducami.studymate.domain.study.dto.UpdateStudyRequest;
import com.ducami.studymate.domain.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody CreateStudyRequest request) {
        studyService.save(request);
        return ResponseEntity.status(201).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UpdateStudyRequest request) {
        studyService.update(id, request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
