package com.ducami.studymate.domain.study.repository;

import com.ducami.studymate.domain.study.entity.StudyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudyRepository extends JpaRepository<StudyEntity, Long> {
}
