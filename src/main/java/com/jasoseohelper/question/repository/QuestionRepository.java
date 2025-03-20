package com.jasoseohelper.question.repository;

import com.jasoseohelper.question.entity.Question;
import com.jasoseohelper.resume.entity.Resume;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    List<Question> findByResume(Resume resume);
}
