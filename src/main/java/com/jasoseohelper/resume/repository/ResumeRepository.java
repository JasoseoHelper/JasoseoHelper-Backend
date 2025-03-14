package com.jasoseohelper.resume.repository;

import com.jasoseohelper.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> { }
