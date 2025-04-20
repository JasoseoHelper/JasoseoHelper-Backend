package com.jasoseohelper.redis.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Getter
@NoArgsConstructor
@RedisHash("TempVersion")
public class RedisTempVersion {

    @Id
    private String vid;

    private String title;
    private String guide;
    private String content;
    private String feedback;

    @Builder
    public RedisTempVersion(String vid, String title, String guide, String content, String feedback) {
        this.vid = vid;
        this.title = title;
        this.guide = guide;
        this.content = content;
        this.feedback = feedback;
    }
}
