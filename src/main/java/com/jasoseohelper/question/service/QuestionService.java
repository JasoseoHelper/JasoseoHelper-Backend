package com.jasoseohelper.question.service;

import com.jasoseohelper.question.dto.QuestionRequstDTO;
import com.jasoseohelper.question.entity.Question;
import com.jasoseohelper.question.repository.QuestionRepository;
import com.jasoseohelper.resume.entity.Resume;
import com.jasoseohelper.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository repository;

    /* 문항 추가 */
    public Question register(Long rid, User user) {
        Resume resume = new Resume();
        resume.setRid(rid);
        Question question = new Question();
        question.setUser(user);
        question.setResume(resume);

        return repository.save(question);
    }
}
