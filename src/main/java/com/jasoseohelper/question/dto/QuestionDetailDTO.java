package com.jasoseohelper.question.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class QuestionDetailDTO {
    private Long qid;
    private String title;
    private String guide;
    private Timestamp m_date;
    private String content;
    private String feedback;
}