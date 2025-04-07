package com.jasoseohelper.question.service;

import com.jasoseohelper.question.dto.VersionRequestDTO;
import com.jasoseohelper.question.dto.VersionResponseDTO;
import com.jasoseohelper.question.entity.Question;
import com.jasoseohelper.question.entity.Version;
import com.jasoseohelper.question.repository.QuestionRepository;
import com.jasoseohelper.question.repository.VersionRepository;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import com.jasoseohelper.user.entity.User;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log4j2
@RequiredArgsConstructor
public class VersionService {
    private final VersionRepository versionRepository;
    private final QuestionRepository questionRepository;

    @Transactional
    public VersionResponseDTO saveVersion(VersionRequestDTO versionRequestDTO, User user){
        Long vid = versionRequestDTO.getVid();
        Version version = versionRepository.findById(vid)
                .orElseThrow(() -> new IllegalArgumentException("Version not found with id: " + vid));

        // qid 버전 업데이트
        Question question = version.getQid();
        question.updateVersion(vid);
        questionRepository.save(question);

        // 제목, 컨텐츠, 피드백, 가이드, m_date (지금 시간) 으로 변경해서 save
        Version updatedVersion = dtoToEntity(versionRequestDTO, question);
        Version savedVersion = versionRepository.save(updatedVersion);

        return entityToDto(savedVersion);
    }

    public Version createIntialVersion(Question question){
        Version build = Version.builder()
                .qid(question)
                .title("")
                .content("")
                .guide("")
                .feedback("")
                .build();
        return versionRepository.save(build);
    }

    private Version dtoToEntity(VersionRequestDTO dto, Question question) {
        return Version.builder()
                .vid(dto.getVid())
                .title(dto.getTitle())
                .content(dto.getContent())
                .guide(dto.getGuide())
                .feedback(dto.getFeedback())
                .qid(question)
                .m_date(Timestamp.valueOf(LocalDateTime.now()))
                .build();
    }

    private VersionResponseDTO entityToDto(Version entity) {
        return VersionResponseDTO.builder()
                .vid(entity.getVid())
                .title(entity.getTitle())
                .content(entity.getContent())
                .guide(entity.getGuide())
                .feedback(entity.getFeedback())
                .m_date(entity.getM_date())
                .qid(entity.getQid().getQid())
                .build();
    }
}
