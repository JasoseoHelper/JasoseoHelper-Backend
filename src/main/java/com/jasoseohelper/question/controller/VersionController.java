package com.jasoseohelper.question.controller;

import com.jasoseohelper.question.dto.VersionRequestDTO;
import com.jasoseohelper.question.dto.VersionResponseDTO;
import com.jasoseohelper.question.service.VersionService;
import com.jasoseohelper.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("/version")
public class VersionController {
    private final VersionService versionService;

    @PostMapping
    public ResponseEntity<VersionResponseDTO> saveQuestion(@RequestBody VersionRequestDTO versionRequestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails){
        return new ResponseEntity<>(versionService.saveVersion(versionRequestDTO, userDetails.getUser()), HttpStatus.OK);
    }
}
