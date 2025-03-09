package com.jasoseohelper.user.controller;

import com.jasoseohelper.security.UserDetailsImpl;
import com.jasoseohelper.user.DTO.UserRequestDTO;
import com.jasoseohelper.user.entity.User;
import com.jasoseohelper.user.service.UserService;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> getUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            return new ResponseEntity<>(userService.getUser(userDetails.getUser()), HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    public ResponseEntity<String> singUp(@RequestBody UserRequestDTO userRequestDTO){
        try {
            userService.signUp(userRequestDTO);
            return new ResponseEntity<>("회원가입 성공", HttpStatus.CREATED);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PatchMapping
    public ResponseEntity<String> updateUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody Map<String, String> updates) {
        try {
            User user = userDetails.getUser();

            // 필드별 업데이트 확인 및 적용
            if (updates.containsKey("email")) {
                userService.updateEmail(user, updates.get("email"));
            }

            if (updates.containsKey("password")) {
                userService.updatePw(user, updates.get("password"));
            }

            return new ResponseEntity<>("사용자 정보 업데이트 성공", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails){
        try {
            userService.delete(userDetails.getUser());
            return new ResponseEntity<>("삭제 성공", HttpStatus.OK);
        }catch (IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
