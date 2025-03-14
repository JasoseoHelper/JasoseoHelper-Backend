package com.jasoseohelper.question.repository;

import com.jasoseohelper.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
