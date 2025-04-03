package com.jasoseohelper.resume.controller;

import com.jasoseohelper.question.dto.QuestionResponseDTO;
import com.jasoseohelper.question.service.QuestionService;
import com.jasoseohelper.resume.dto.ResumeRequestDTO;
import com.jasoseohelper.resume.dto.ResumeResponseDTO;
import com.jasoseohelper.resume.service.ResumeService;
import com.jasoseohelper.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/resume")
public class ResumeController {
    private final ResumeService service;
    private final QuestionService questionService;

    @GetMapping
    public ResponseEntity<Page<ResumeResponseDTO>> getResumes(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "10") int size){
        Page<ResumeResponseDTO> response = service.getUserResumes(userDetails.getUser().getUid(), page, size);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResumeResponseDTO> registerResume(@RequestBody ResumeRequestDTO resumeRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(service.register(resumeRequestDTO, userDetails.getUser()), HttpStatus.CREATED);
    }

    @PatchMapping("/{rid}")
    public ResponseEntity<ResumeResponseDTO> modifyResume(@PathVariable("rid") Long rid, @RequestBody ResumeRequestDTO resumeRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(service.modify(resumeRequestDTO, rid, userDetails.getUser()), HttpStatus.OK);
    }

    @DeleteMapping("/{rid}")
    public ResponseEntity<Boolean> deleteResume(@PathVariable("rid") Long rid, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(service.delete(rid, userDetails.getUser()), HttpStatus.OK);
    }

    @GetMapping("/{rid}")
    public ResponseEntity<List<QuestionResponseDTO>> getQuestionByResume(@PathVariable("rid") Long rid, @AuthenticationPrincipal
    UserDetailsImpl userDetails){
        List<QuestionResponseDTO> questions = questionService.getQuestionsByResume(rid, userDetails.getUser());
        return new ResponseEntity<>(questions, HttpStatus.OK);
    }

}
