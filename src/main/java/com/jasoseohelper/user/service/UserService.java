package com.jasoseohelper.user.service;

import com.jasoseohelper.user.DTO.UserRequestDTO;
import com.jasoseohelper.user.entity.User;
import com.jasoseohelper.user.repository.UserRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void signUp(UserRequestDTO userRequestDTO){
        String email = userRequestDTO.getEmail();
        String pw = userRequestDTO.getPw();
        String confirmPw = userRequestDTO.getConfirmPw();
        String name = userRequestDTO.getName();

        if(!pw.equals(confirmPw))
            throw new IllegalArgumentException("비밀번호가 다릅니다.");

        if(userRepository.findByEmail(email).isPresent())
            throw new IllegalArgumentException("이미 가입된 계정입니다");

        User user = User.builder().
                email(email).
                pw(passwordEncoder.encode(pw)).
                name(name).
                build();

        userRepository.save(user);
    }

    public void delete(User user){
        userRepository.delete(user);
    }

    public Map<String, String> getUser(User user){
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("email", user.getEmail());
        responseBody.put("name", user.getName());
        return responseBody;
    }

    public void updateEmail(User user, String email){
        // 이메일 중복 확인
        if (userRepository.findByEmail(email).isPresent() &&
                !user.getEmail().equals(email)) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        user.setEmail(email);
        userRepository.save(user);
    }

    public void updatePw(User user, String pw) {
        // 비밀번호 복잡성 검증 (선택적)
        if (pw == null || pw.isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 비어있을 수 없습니다.");
        }

        // 비밀번호 암호화 후 저장
        user.setPw(passwordEncoder.encode(pw));
        userRepository.save(user);
    }
}
