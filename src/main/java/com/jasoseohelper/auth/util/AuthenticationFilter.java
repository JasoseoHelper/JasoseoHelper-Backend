package com.jasoseohelper.auth.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasoseohelper.auth.dto.AuthRequestDTO;
import com.jasoseohelper.security.UserDetailsImpl;
import com.jasoseohelper.user.entity.RefreshToken;
import com.jasoseohelper.user.entity.User;
import com.jasoseohelper.user.repository.RefreshTokenRepository;
import com.jasoseohelper.user.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public AuthenticationFilter(TokenProvider tokenProvider, RefreshTokenRepository refreshTokenRepository, UserRepository userRepository){
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        setFilterProcessesUrl("/members/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AuthRequestDTO requestDto = new ObjectMapper().readValue(request.getInputStream(), AuthRequestDTO.class);

            User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(() -> new RuntimeException("존재하지 않는 회원입니다."));
            if(user != null){
                return getAuthenticationManager().authenticate(
                        new UsernamePasswordAuthenticationToken(
                                requestDto.getEmail(),
                                requestDto.getPw(),
                                null
                        )
                );
            }else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                writeResponse(response, "존재하지 않는 회원입니다.");
                return null;
            }

        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String email = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        String token = tokenProvider.createToken(email);
        String refresh = tokenProvider.createRefreshToken(email);

        RefreshToken refreshToken = refreshTokenRepository.findByEmail(email).orElse(null);
        if (refreshToken == null) {
            refreshToken = new RefreshToken(refresh, email);
        } else {
            refreshToken.updateToken(refresh);
        }
        refreshTokenRepository.save(refreshToken);
        response.addHeader(TokenProvider.AUTHORIZATION_HEADER, token);
        response.addHeader(TokenProvider.REFRESH_HEADER, refreshToken.getToken());
        response.setContentType("application/json; charset=UTF-8");

//        response.setStatus(HttpServletResponse.SC_OK);
//        writeResponse(response, "로그인 성공");
        // response body에 토큰 정보 추가
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        responseBody.put("refreshToken", refreshToken.getToken());

        // response body에 토근 정보 출력
        try (PrintWriter writer = response.getWriter()) {
            writer.write(new ObjectMapper().writeValueAsString(responseBody));
        }
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        writeResponse(response, "아이디 또는 비밀번호가 틀렸습니다.");
    }

    private void writeResponse(HttpServletResponse response, String message) {
        try {
            response.setContentType("text/plain;charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
        }
    }
}