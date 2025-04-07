package com.jasoseohelper.question.controller;

import com.jasoseohelper.question.dto.QuestionRequestDTO;
import com.jasoseohelper.question.dto.QuestionResponseDTO;
import com.jasoseohelper.question.service.QuestionService;
import com.jasoseohelper.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService questionService;

    @GetMapping("/{qid}")
    public ResponseEntity<QuestionResponseDTO> getQuestionByQid(@PathVariable("qid") Long qid, @AuthenticationPrincipal
    UserDetailsImpl userDetails){
        return new ResponseEntity<>(questionService.getQuestion(qid, userDetails.getUser()), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<QuestionResponseDTO> saveQuestion(@RequestBody QuestionRequestDTO questionRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(questionService.saveQuestion(questionRequestDTO, userDetails.getUser()), HttpStatus.OK);
    }

    @PostMapping("/{rid}")
    public ResponseEntity<QuestionResponseDTO> addQuestion(@PathVariable("rid") Long rid, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(questionService.addQuestion(rid ,userDetails.getUser()), HttpStatus.OK);
    }

    @DeleteMapping("/{qid}")
    public ResponseEntity<Boolean> deleteQuestion(@PathVariable("qid") Long qid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(questionService.deleteQuestion(qid, userDetails.getUser()), HttpStatus.OK);
    }
}
