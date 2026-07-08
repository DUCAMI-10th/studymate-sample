package com.ducami.studymate.domain.study.service;

import com.ducami.studymate.domain.study.dto.request.CreateStudyRequest;
import com.ducami.studymate.domain.study.dto.response.StudyResponse;
import com.ducami.studymate.domain.study.dto.response.StudySummaryResponse;
import com.ducami.studymate.domain.study.dto.request.UpdateStudyRequest;
import com.ducami.studymate.domain.study.entity.StudyEntity;
import com.ducami.studymate.domain.study.exception.StudyNotFoundException;
import com.ducami.studymate.domain.study.repository.StudyRepository;
import com.ducami.studymate.domain.user.entity.UserEntity;
import com.ducami.studymate.domain.user.repository.UserRepository;
import com.ducami.studymate.global.exception.AuthenticatedUserNotFoundException;
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
    private final UserRepository userRepository;

    public StudyResponse findById(Long id) {
        return StudyResponse.from(findStudyOrThrow(id));
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
    public void update(Long id, UpdateStudyRequest request, Long currentUserId) {
        StudyEntity study = findStudyOrThrow(id);
        study.validateOwner(currentUserId);
        study.update(request);
    }

    @Transactional
    public void delete(Long id, Long currentUserId) {
        StudyEntity study = findStudyOrThrow(id);
        study.validateOwner(currentUserId);
        studyRepository.delete(study);
    }

    @Transactional
    public Long save(CreateStudyRequest request, Long currentUserId) {
        UserEntity currentUser = findCurrentUserOrThrow(currentUserId);
        StudyEntity entity = new StudyEntity(request, currentUser);
        return studyRepository.save(entity).getId();
    }

    private StudyEntity findStudyOrThrow(Long id) {
        Optional<StudyEntity> studyOptional = studyRepository.findById(id);

        if (studyOptional.isEmpty()) {
            throw new StudyNotFoundException();
        }

        return studyOptional.get();
    }

    private UserEntity findCurrentUserOrThrow(Long currentUserId) {
        Optional<UserEntity> userOptional = userRepository.findById(currentUserId);

        if (userOptional.isEmpty()) {
            throw new AuthenticatedUserNotFoundException();
        }

        return userOptional.get();
    }

}
