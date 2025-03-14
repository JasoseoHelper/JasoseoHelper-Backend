package com.jasoseohelper.resume.dto;

import lombok.*;

import java.sql.Timestamp;

@ToString
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeResponseDTO {
    private Long rid;
    private Long uid;
    private String resume_name;
    private Timestamp d_date;
    private Timestamp c_date;
    private Timestamp m_date;
}
