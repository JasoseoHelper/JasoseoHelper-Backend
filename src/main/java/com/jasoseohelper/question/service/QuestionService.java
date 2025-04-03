package com.jasoseohelper.question.service;

import com.jasoseohelper.question.dto.QuestionDetailDTO;
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

    /* 문항 버전 리스트 불러오기 */
    public List<QuestionDetailDTO> getVersionsByQuestion(Long qid, User user){
        Question question = existQuestion(qid, user);
        List<Version> versions = versionRepository.findByQuestion(question);
        return versions.stream().map(this::EntityToDtoForVersion).collect(Collectors.toList());
    }

    /* 특정 버전 불러오기 */
    public QuestionDetailDTO getVersion(Long qid, Long vid, User user){
        Version version = existVersion(qid, vid, user);
        return EntityToDtoForVersion(version);
    }

    /* 존재하는 Question 반환. 없을 때, 접근 제한 있을 때 Exception 발생 */
    private Question existQuestion(Long qid, User user){
        // 존재하는 Question 인지
        Question question = questionRepository.findById(qid).orElseThrow(()-> new NoSuchElementException("Question not found for qid: " + qid));
        // 해당 Question 접근 권한이 있는 user 인지
        if(! question.getUser().getUid().equals(user.getUid())) throw new AccessDeniedException("Forbidden for qid: " + qid + " and uid: " + user.getUid());
        return question;
    }

    /* 존재하는 Version 반환. 없을 때, 접근 제한 있을 때 Exception 발생 */
    private Version existVersion(Long qid, Long vid, User user){
        Question question = existQuestion(qid, user);
        // 존재하는 Version 인지
        Version version = versionRepository.findByQuestionAndVid(question, vid)
                .orElseThrow(()-> new NoSuchElementException("Question Version not found for qid: " + qid+" / vid: "+vid));
        return version;
    }


    public QuestionDetailDTO EntityToDtoForVersion(Version version) {
        return QuestionDetailDTO.builder()
                .qid(version.getQuestion().getQid())
                .title(version.getTitle())
                .guide(version.getGuide())
                .m_date(version.getM_date())
                .content(version.getContent())
                .feedback(version.getFeedback())
                .build();
    }

    public QuestionDetailDTO DtoToEntityForVersion(Version version) {
        return QuestionDetailDTO.builder()
                .qid(version.getQuestion().getQid())
                .title(version.getTitle())
                .guide(version.getGuide())
                .m_date(version.getM_date())
                .content(version.getContent())
                .feedback(version.getFeedback())
                .build();
    }
}
