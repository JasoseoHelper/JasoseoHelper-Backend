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
public class QuestionResponseDTO {
    private Long qid;
    private String title;
    private String guide;
    private Timestamp c_date;
    private Timestamp m_date;
    private String content;
    private String feedback;
}