package com.jasoseohelper;

import com.jasoseohelper.config.redis.EmbeddedRedisConfig;
import com.jasoseohelper.question.entity.Version;
import com.jasoseohelper.question.repository.VersionRepository;
import com.jasoseohelper.redis.entity.RedisTempVersion;
import com.jasoseohelper.redis.repository.RedisVersionRepository;
import com.jasoseohelper.redis.service.RedisVersionService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ActiveProfiles("local")
@SpringBootTest
@Import(EmbeddedRedisConfig.class)
public class RedisVersionServiceTest {

    @Autowired
    private RedisVersionService redisVersionService;  // 서비스 빈 주입

    @Autowired
    private RedisVersionRepository redisVersionRepository;  // 레디스 리포지토리 주입

    @Autowired
    private VersionRepository versionRepository;  // 실제 DB 리포지토리 주입

    private Version originalVersion;

    @BeforeEach
    void setup() {
        // 테스트 전에 원본 버전 저장
        Long versionId = 1L;
        originalVersion = versionRepository.findById(versionId)
                .orElse(null);  // 없을 수도 있으므로 null 허용
    }

    @Test
    void testSaveTempVersion() {
        Long versionId = 999L;  // 테스트용 versionId
        String title = "Temporary Title";
        String guide = "Temporary Guide";
        String content = "Temporary Content";
        String feedback = "Temporary Feedback";

        // 임시 저장
        redisVersionService.saveTempVersion(versionId, title, guide, content, feedback);

        // 레디스에 저장된 데이터 확인
        RedisTempVersion tempVersion = redisVersionRepository.findById(String.valueOf(versionId)).orElseThrow();
        assertEquals(versionId.toString(), tempVersion.getVid());
        assertEquals(title, tempVersion.getTitle());
        assertEquals(guide, tempVersion.getGuide());
        assertEquals(content, tempVersion.getContent());
        assertEquals(feedback, tempVersion.getFeedback());
    }

    @Test
    void testPersistLatestVersion() {
        Long versionId = 1L;
        String title = "Updated Title";
        String guide = "Updated Guide";
        String content = "Updated Content";
        String feedback = "Updated Feedback";

        // 레디스에 임시 버전 저장
        redisVersionService.saveTempVersion(versionId, "Updated Title", "Updated Guide", "Updated Content", "Updated Feedback");

        // 트리거가 발동하여 DB에 저장
        redisVersionService.persistLatestVersion(versionId);

        // DB에서 해당 Version 확인
        Version version = versionRepository.findById(versionId).orElseThrow();
        assertEquals(title, version.getTitle());
        assertEquals(guide, version.getGuide());
        assertEquals(content, version.getContent());
        assertEquals(feedback, version.getFeedback());

        // 레디스에서 삭제된 것 확인
        boolean existsInRedis = redisVersionRepository.existsById(String.valueOf(versionId));
        assertFalse(existsInRedis);
    }

    @AfterEach
    void cleanup() {
        // 테스트 후에 원래 버전으로 복원
        Long versionId = 1L;
        if (originalVersion != null) {
            versionRepository.save(originalVersion);
        } else {
            // 원본 데이터가 없었다면 테스트에서 생성된 데이터 삭제
            versionRepository.deleteById(versionId);
        }
    }
}

