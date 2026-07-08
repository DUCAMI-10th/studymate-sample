package com.ducami.studymate.domain.study.service;

import com.ducami.studymate.domain.study.dto.StudyResponse;
import com.ducami.studymate.domain.study.dto.StudySummaryResponse;
import com.ducami.studymate.domain.study.entity.StudyEntity;
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
        Optional<StudyEntity> studyOptional = studyRepository.findById(id);

        if (studyOptional.isEmpty()) {
            throw new IllegalArgumentException("스터디가 존재하지 않습니다.");
        }

        StudyEntity study = studyOptional.get();
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

}
