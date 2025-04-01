package com.jasoseohelper.question.controller;

import com.jasoseohelper.question.dto.QuestionResponseDTO;
import com.jasoseohelper.question.service.QuestionService;
import com.jasoseohelper.security.UserDetailsImpl;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/resume")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/{rid}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionByResume(@PathVariable("rid") Long rid, @AuthenticationPrincipal
                                                                         UserDetailsImpl userDetails){
        List<QuestionResponseDTO> questions = questionService.getQuestionsByResume(rid, userDetails.getUser());
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }
}
