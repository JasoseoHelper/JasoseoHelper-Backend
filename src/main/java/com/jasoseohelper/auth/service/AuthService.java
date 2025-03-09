package com.jasoseohelper.auth.service;

import com.jasoseohelper.auth.dto.AuthRequestDTO;
import com.jasoseohelper.user.DTO.UserResponseDTO;
import com.jasoseohelper.user.entity.User;
import com.jasoseohelper.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponseDTO login(AuthRequestDTO authRequestDTO){
        User user = userRepository.findByEmail(authRequestDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("입력값 확인"));
        if(!passwordEncoder.matches(authRequestDTO.getPw(), user.getPw())){
            throw new IllegalArgumentException("입력값 확인");
        };

        return new UserResponseDTO(user.getEmail(), user.getName());
    }

    public void sendPasswordResetMail(User user){
        // 이메일 전송하는 로직
    }

}
