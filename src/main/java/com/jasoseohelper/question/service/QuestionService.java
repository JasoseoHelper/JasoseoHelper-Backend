package com.jasoseohelper.question.service;

import com.jasoseohelper.question.dto.QuestionRequestDTO;
import com.jasoseohelper.question.dto.QuestionResponseDTO;
import com.jasoseohelper.question.entity.Question;
import com.jasoseohelper.question.entity.Version;
import com.jasoseohelper.question.repository.QuestionRepository;
import com.jasoseohelper.question.repository.VersionRepository;
import com.jasoseohelper.resume.entity.Resume;
import com.jasoseohelper.resume.repository.ResumeRepository;
import com.jasoseohelper.user.entity.User;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final ResumeRepository resumeRepository;
    private final VersionRepository versionRepository;
    private final VersionService versionService;

    /* 자소서 문항 조회 (qid, title) */
    public List<QuestionResponseDTO> getQuestionsByResume(Long rid, User user){
        Resume resume = existResume(rid, user);

        List<Question> questions = questionRepository.findByResume(resume);
        return questions.stream()
                .map(q -> {
                    Optional<Version> versionOpt = versionRepository.findById(q.getVersion());
                    String title = versionOpt.map(Version::getTitle).orElse("제목 없음");

                    return QuestionResponseDTO.builder()
                            .qid(q.getQid())
                            .title(title)
                            .build();
                })
                .collect(Collectors.toList());
    }

    /* 문항 조회 */
    public QuestionResponseDTO getQuestion(Long qid, User user){
        Question question = existQuestion(qid, user);

        Version version = versionRepository.findLatestVersionByQid(question)
                .orElseThrow(() -> new IllegalStateException("No version found for this question"));

        return QuestionResponseDTO.builder()
                .qid(question.getQid())
                .title(version.getTitle())
                .content(version.getContent())
                .c_date(question.getC_date())
                .guide(version.getGuide())
                .feedback(version.getFeedback())
                .build();
    }

    /* 문항 저장 */
    public QuestionResponseDTO saveQuestion(QuestionRequestDTO questionRequestDTO, User user){
        Resume resume = existResume(questionRequestDTO.getRid(), user);

        Question build = Question.builder()
                .version(questionRequestDTO.getVersion())
                .resume(resume)
                .user(user)
                .build();
        Question save = questionRepository.save(build);

        return getQuestion(save.getQid(), user);
    }

    /* 문항 추가 */
    @Transactional
    public QuestionResponseDTO addQuestion(Long rid, User user) {
        Resume resume = existResume(rid, user);

        Question question = questionRepository.save(Question.builder()
                .resume(resume)
                .user(user)
                .build());

        // version save
        Version version = versionService.createIntialVersion(question);

        question.updateVersion(version.getVid());
        Question save = questionRepository.save(question);

        return getQuestion(save.getQid(), user);
    }

    /* 문항 삭제 */
    @Transactional
    public boolean deleteQuestion(Long qid, User user){
        Question question = existQuestion(qid, user);

        versionRepository.deleteAllByQid(question);

        questionRepository.delete(question);

        return true;
    }

    /* 존재하는 Resume 반환. 없으면 Exception 발생 */
    private Resume existResume(Long rid, User user){
        // 존재하는 resume 인지
        Resume resume = resumeRepository.findById(rid).orElseThrow(()-> new NoSuchElementException("Resume not found for rid: " + rid));
        // 해당 resume에 접근 권한이 있는 user 인지
        if(! resume.getUser().getUid().equals(user.getUid())) throw new AccessDeniedException("Forbidden for rid: " + rid + " and uid: " + user.getUid());
        return resume;
    }

    /* 존재하는 Question 반환. 없으면 Exception 발생 */
    private Question existQuestion(Long qid, User user){
        Question question = questionRepository.findById(qid)
                .orElseThrow(() -> new IllegalArgumentException("Question not found with id: " + qid));
        // 해당 resume에 접근 권한이 있는 user 인지
        if(!question.getUser().getUid().equals(user.getUid())){
            throw new AccessDeniedException("You don't have access to this question.");
        }
        return question;
    }
}
