package com.jasoseohelper.question.controller;

import com.jasoseohelper.question.dto.QuestionDetailDTO;
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
@RequestMapping("/question")
public class QuestionController {
    private final QuestionService service;

    @GetMapping("{qid}/version")
    public ResponseEntity<List<QuestionDetailDTO>> getVersionsByQuestion(@PathVariable("qid") Long qid,
                                                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(service.getVersionsByQuestion(qid, userDetails.getUser()), HttpStatus.OK);
    }

    @GetMapping("{qid}/version/{vid}")
    public ResponseEntity<QuestionDetailDTO> getVersion(@PathVariable("qid") Long qid, @PathVariable("vid") Long vid,
                                                        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return new ResponseEntity<>(service.getVersion(qid, vid, userDetails.getUser()), HttpStatus.OK);
    }
}
