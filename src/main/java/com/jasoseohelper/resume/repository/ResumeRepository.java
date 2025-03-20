package com.jasoseohelper.resume.repository;

import com.jasoseohelper.resume.entity.Resume;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    Page<Resume> findByUser_Uid(Long uid, Pageable pageable);
}
