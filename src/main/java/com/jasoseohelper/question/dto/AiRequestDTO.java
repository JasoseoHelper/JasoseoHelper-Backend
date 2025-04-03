package com.jasoseohelper.question.dto;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class AiRequestDTO {
    private Long qid;
    private String title;
    private String content;
}
