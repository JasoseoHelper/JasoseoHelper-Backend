package com.jasoseohelper.question.dto;

import java.sql.Timestamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VersionResponseDTO {
    private Long vid;
    private String title;
    private String content;
    private String guide;
    private String feedback;
    private Timestamp m_date;
    private Long qid;
}
