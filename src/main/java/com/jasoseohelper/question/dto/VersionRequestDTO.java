package com.jasoseohelper.question.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VersionRequestDTO {
    private Long vid;
    private String title;
    private String content;
    private String guide;
    private String feedback;
}
