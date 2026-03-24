package com.ducami.studymate.domain.study.service;

import com.ducami.studymate.domain.study.dto.request.CreateStudyRequest;
import com.ducami.studymate.domain.study.dto.response.StudyResponse;
import com.ducami.studymate.domain.study.dto.response.StudySummaryResponse;
import com.ducami.studymate.domain.study.dto.request.UpdateStudyRequest;
import com.ducami.studymate.domain.study.entity.StudyEntity;
import com.ducami.studymate.domain.study.exception.StudyStatusCode;
import com.ducami.studymate.domain.study.repository.StudyRepository;
import com.ducami.studymate.global.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyServiceImpl implements StudyService {
    private final StudyRepository studyRepository;

    @Override
    public StudyResponse findById(Long id) {
        return studyRepository.findById(id)
            .map(StudyResponse::toEntity)
            .orElseThrow(() -> new BusinessException(StudyStatusCode.STUDY_NOT_FOUND));
    }

    @Override
    public List<StudySummaryResponse> findAll() {
        return studyRepository.findAll().stream()
                .map(StudySummaryResponse::toEntity)
                .toList();
    }

    @Override
    @Transactional
    public void update(Long id, UpdateStudyRequest request) {
        StudyEntity study = studyRepository.findById(id)
            .orElseThrow(() -> new BusinessException(StudyStatusCode.STUDY_NOT_FOUND));

        study.update(request);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        studyRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Long save(CreateStudyRequest request) {
        StudyEntity entity = new StudyEntity(request);
        return studyRepository.save(entity).getId();
    }
}
