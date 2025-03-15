package com.jasoseohelper.resume.controller;

import com.jasoseohelper.resume.dto.ResumeRequestDTO;
import com.jasoseohelper.resume.dto.ResumeResponseDTO;
import com.jasoseohelper.resume.service.ResumeService;
import com.jasoseohelper.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/resume")
public class ResumeController {
    private final ResumeService service;

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

}
