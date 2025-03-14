package com.jasoseohelper.resume.repository;

import com.jasoseohelper.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Optional<Resume> findByRidAndUid(Long rid, Long uid);
}
