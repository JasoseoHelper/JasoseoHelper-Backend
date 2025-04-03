package com.jasoseohelper.question.repository;

import com.jasoseohelper.question.entity.Question;
import com.jasoseohelper.question.entity.Version;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VersionRepository extends JpaRepository<Version, Long> {
    Optional<Version> findByQuestionAndVid(Question question, Long vid);
    List<Version> findByQuestion(Question question);
}
