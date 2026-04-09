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

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StudyServiceImpl implements StudyService {
    private final StudyRepository studyRepository;
    private final UserRepository userRepository;

    @Override
    public StudyResponse findById(Long id) {
        return StudyResponse.from(findStudyOrThrow(id));
    }

    @Override
    public List<StudySummaryResponse> findAll() {
        return studyRepository.findAll().stream()
                .map(StudySummaryResponse::from)
                .toList();
    }

    @Override
    @Transactional
    public void update(Long id, UpdateStudyRequest request, Long currentUserId) {
        StudyEntity study = findStudyOrThrow(id);
        study.validateOwner(currentUserId);
        study.update(request);
    }

    @Override
    @Transactional
    public void delete(Long id, Long currentUserId) {
        StudyEntity study = findStudyOrThrow(id);
        study.validateOwner(currentUserId);
        studyRepository.delete(study);
    }

    @Override
    @Transactional
    public Long save(CreateStudyRequest request, Long currentUserId) {
        UserEntity currentUser = findCurrentUserOrThrow(currentUserId);
        StudyEntity entity = new StudyEntity(request, currentUser);
        return studyRepository.save(entity).getId();
    }

    private StudyEntity findStudyOrThrow(Long id) {
        return studyRepository.findById(id)
                .orElseThrow(StudyNotFoundException::new);
    }

    private UserEntity findCurrentUserOrThrow(Long currentUserId) {
        return userRepository.findById(currentUserId)
                .orElseThrow(AuthenticatedUserNotFoundException::new);
    }

}
