package com.jasoseohelper.resume.dto;

import lombok.*;

import java.sql.Timestamp;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResumeRequestDTO {
    private String resumeName;
    private Timestamp d_date;
}
