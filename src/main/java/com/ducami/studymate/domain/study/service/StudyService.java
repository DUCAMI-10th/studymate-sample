package com.ducami.studymate.domain.study.service;

import com.ducami.studymate.domain.study.dto.request.CreateStudyRequest;
import com.ducami.studymate.domain.study.dto.response.StudyResponse;
import com.ducami.studymate.domain.study.dto.response.StudySummaryResponse;
import com.ducami.studymate.domain.study.dto.request.UpdateStudyRequest;
import com.ducami.studymate.domain.study.entity.StudyEntity;
import com.ducami.studymate.domain.study.exception.StudyNotFoundException;
import com.ducami.studymate.domain.study.repository.StudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyService {
    private final StudyRepository studyRepository;

    public StudyResponse findById(Long id) {
        StudyEntity study = findStudyOrThrow(id);
        return StudyResponse.from(study);
    }

    public List<StudySummaryResponse> findAll() {
        List<StudyEntity> studies = studyRepository.findAll();
        List<StudySummaryResponse> responses = new ArrayList<>();

        for (StudyEntity study : studies) {
            StudySummaryResponse response = StudySummaryResponse.from(study);
            responses.add(response);
        }

        return responses;
    }

    @Transactional
    public void update(Long id, UpdateStudyRequest request) {
        StudyEntity study = findStudyOrThrow(id);
        study.update(request);
    }

    @Transactional
    public void delete(Long id) {
        studyRepository.deleteById(id);
    }

    @Transactional
    public Long save(CreateStudyRequest request) {
        StudyEntity entity = new StudyEntity(request);
        return studyRepository.save(entity).getId();
    }

    private StudyEntity findStudyOrThrow(Long id) {
        Optional<StudyEntity> studyOptional = studyRepository.findById(id);

        if (studyOptional.isEmpty()) {
            throw new StudyNotFoundException();
        }

        return studyOptional.get();
    }
}
