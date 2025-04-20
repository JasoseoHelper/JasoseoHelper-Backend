package com.jasoseohelper.redis.service;

import com.jasoseohelper.question.entity.Version;
import com.jasoseohelper.question.repository.VersionRepository;
import com.jasoseohelper.redis.entity.RedisTempVersion;
import com.jasoseohelper.redis.repository.RedisVersionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisVersionService {

    private final RedisVersionRepository redisVersionRepository;
    private final VersionRepository versionRepository;

    /**
     * Redis에 임시 저장 (작성 중)
     */
    public void saveTempVersion(Long versionId, String title, String guide, String content, String feedback) {
        RedisTempVersion temp = RedisTempVersion.builder()
                .vid(String.valueOf(versionId)) // ← 필드명 변경 반영
                .title(title)
                .guide(guide)
                .content(content)
                .feedback(feedback)
                .build();

        redisVersionRepository.save(temp);
    }

    /**
     * 트리거 발생 시, Redis에서 가져와 DB 갱신
     */
    public void persistLatestVersion(Long versionId) {
        redisVersionRepository.findById(String.valueOf(versionId)).ifPresent(temp -> {
            Version version = versionRepository.findById(versionId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 Version입니다."));

            version.updateFrom(temp);
            versionRepository.save(version);

            // Redis에서 삭제
            redisVersionRepository.deleteById(temp.getVid());
        });
    }

}
