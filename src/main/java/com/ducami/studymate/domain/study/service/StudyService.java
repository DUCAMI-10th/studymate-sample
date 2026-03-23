package com.ducami.studymate.domain.study.service;

import com.ducami.studymate.domain.study.dto.request.CreateStudyRequest;
import com.ducami.studymate.domain.study.dto.response.StudyResponse;
import com.ducami.studymate.domain.study.dto.response.StudySummaryResponse;
import com.ducami.studymate.domain.study.dto.request.UpdateStudyRequest;

import java.util.List;

public interface StudyService {

    StudyResponse findById(Long id);

    List<StudySummaryResponse> findAll();

    void update(Long id, UpdateStudyRequest request);

    void delete(Long id);

    Long save(CreateStudyRequest request);
}
