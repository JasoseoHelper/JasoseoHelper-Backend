package com.jasoseohelper.resume.service;

import com.jasoseohelper.question.entity.Question;
import com.jasoseohelper.question.repository.QuestionRepository;
import com.jasoseohelper.resume.entity.Resume;
import com.jasoseohelper.resume.dto.ResumeRequestDTO;
import com.jasoseohelper.resume.dto.ResumeResponseDTO;
import com.jasoseohelper.resume.repository.ResumeRepository;
import com.jasoseohelper.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.NoSuchElementException;

@Service
@Log4j2
@RequiredArgsConstructor
public class ResumeService {
    private final ResumeRepository repository;
    private final QuestionRepository questionRepository;

    /* 자소서 추가 */
    @Transactional
    public ResumeResponseDTO register(ResumeRequestDTO resumeRequestDTO, User user) {
        Resume resume = dtoToEntity(resumeRequestDTO, user);
        Resume savedResume = repository.save(resume);

        // 자소서 첫번째 문항 생성
        Question question = new Question();
        question.setResume(savedResume);
        question.setUser(user);
        questionRepository.save(question);

        return entityToDto(savedResume);
    }


    /* 자소서 수정 */
    public ResumeResponseDTO modify(ResumeRequestDTO resumeRequestDTO, Long rid, User user) {
        Resume resume = existResume(rid, user);
        resume.setResume_name(resumeRequestDTO.getResumeName());
        resume.setD_date(resumeRequestDTO.getD_date());
        Timestamp modifyDate = Timestamp.from(ZonedDateTime.now().toInstant());
        resume.setM_date(modifyDate);
        Resume savedResume = repository.save(resume);
        if(!savedResume.getM_date().equals(modifyDate)) throw new IllegalStateException("Faild Resume modify for request: "+resumeRequestDTO);
        return entityToDto(savedResume);
    }

    /* 자소서 삭제 */
    public boolean delete(Long rid, User user) {
        existResume(rid, user);
        repository.deleteById(rid);
        if(repository.existsById(rid)) throw new IllegalStateException("Faild Resume delete for rid: " + rid + " and uid: " + user.getUid());
        return true;
    }

    /* 존재하는 Resume 반환. 없으면 Exception 발생 */
    private Resume existResume(Long rid, User user){
        // 존재하는 resume 인지
        Resume resume = repository.findById(rid).orElseThrow(()-> new NoSuchElementException("Resume not found for rid: " + rid));
        // 해당 resume에 접근 권한이 있는 user 인지
        if(! resume.getUser().getUid().equals(user.getUid())) throw new AccessDeniedException("Forbidden for rid: " + rid + " and uid: " + user.getUid());
        return resume;
    }

    public Resume dtoToEntity(ResumeRequestDTO resumeDTO, User user) {
        return Resume.builder()
                .user(user)
                .resume_name(resumeDTO.getResumeName())
                .d_date(resumeDTO.getD_date())
                .build();
    }

    public ResumeResponseDTO entityToDto(Resume resume) {
        return ResumeResponseDTO.builder()
                .rid(resume.getRid())
                .uid(resume.getUser().getUid())
                .resume_name(resume.getResume_name())
                .d_date(resume.getD_date())
                .c_date(resume.getC_date())
                .m_date(resume.getM_date())
                .build();
    }


}
