package com.jasoseohelper.user.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long refreshtoken_id;
    private String token;
    private String email;

    public RefreshToken(String token, String email){
        this.token = token;
        this.email = email;
    }

    public void updateToken(String token) {
        this.token = token;
    }
}
