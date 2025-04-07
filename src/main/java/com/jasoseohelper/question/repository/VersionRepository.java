package com.jasoseohelper.question.repository;

import com.jasoseohelper.question.entity.Question;
import com.jasoseohelper.question.entity.Version;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface VersionRepository extends JpaRepository<Version, Long> {
    @Query("SELECT v FROM Version v WHERE v.qid = :qid ORDER BY v.m_date DESC LIMIT 1")
    Optional<Version> findLatestVersionByQid(@Param("qid") Question qid);
    void deleteAllByQid(Question question);
    Optional<Version> findByQuestionAndVid(Question question, Long vid);
    List<Version> findByQuestion(Question question);
}
