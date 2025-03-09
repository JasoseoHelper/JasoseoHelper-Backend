package com.jasoseohelper.auth.controller;

import com.jasoseohelper.auth.dto.AuthRequestDTO;
import com.jasoseohelper.auth.service.AuthService;
import com.jasoseohelper.auth.util.TokenProvider;
import com.jasoseohelper.security.UserDetailsImpl;
import com.jasoseohelper.user.DTO.UserResponseDTO;
import com.jasoseohelper.user.service.UserService;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final TokenProvider tokenProvider;
    private final AuthService authService;

    public AuthController(TokenProvider tokenProvider, AuthService authService) {
        this.tokenProvider = tokenProvider;
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequestDTO authRequestDTO){
        try {
            UserResponseDTO userResponseDTO = authService.login(authRequestDTO);
            String accessToken = tokenProvider.createToken(userResponseDTO.getEmail());
            String refreshToken = tokenProvider.createRefreshToken(userResponseDTO.getEmail());

            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("message", "로그인 성공");
            responseBody.put("accessToken", accessToken);
            responseBody.put("refreshToken", refreshToken);
            responseBody.put("user", userResponseDTO);

            return new ResponseEntity<>(responseBody, HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/password-reset")
    public ResponseEntity<String> passwordReset(@AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            authService.sendPasswordResetMail(userDetails.getUser());
            return new ResponseEntity<>("메일 전송 완료", HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }
}
