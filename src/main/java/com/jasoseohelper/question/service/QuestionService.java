package com.jasoseohelper.question.service;

import com.jasoseohelper.question.dto.QuestionResponseDTO;
import com.jasoseohelper.question.entity.Question;
import com.jasoseohelper.question.entity.Version;
import com.jasoseohelper.question.repository.QuestionRepository;
import com.jasoseohelper.question.repository.VersionRepository;
import com.jasoseohelper.resume.entity.Resume;
import com.jasoseohelper.resume.repository.ResumeRepository;
import com.jasoseohelper.user.entity.User;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final ResumeRepository resumeRepository;
    private final VersionRepository versionRepository;

    /* 자소서 문항 조회 (qid, title) */
    public List<QuestionResponseDTO> getQuestionsByResume(Long rid, User user){
        Resume resume = resumeRepository.findById(rid)
                .orElseThrow(() -> new IllegalArgumentException("Resume not found with id: " + rid));

        if(!resume.getUser().getUid().equals(user.getUid())){
            throw new AccessDeniedException("You don't have access to this resume.");
        }

        List<Question> questions = questionRepository.findByResume(resume);
        return questions.stream()
                .map(q -> {
                    Optional<Version> versionOpt = versionRepository.findById(q.getVersion());
                    String title = versionOpt.map(Version::getTitle).orElse("제목 없음");
                    return new QuestionResponseDTO(q.getQid(), title);
                })
                .collect(Collectors.toList());
    }

    /* 문항 추가 */
    public Question register(Long rid, User user) {
        Resume resume = new Resume();
        resume.setRid(rid);
        Question question = new Question();
        question.setUser(user);
        question.setResume(resume);

        return questionRepository.save(question);
    }
}
