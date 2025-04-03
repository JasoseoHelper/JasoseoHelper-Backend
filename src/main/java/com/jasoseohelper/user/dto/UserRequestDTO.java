package com.jasoseohelper.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequestDTO {
    private String email;
    private String pw;
    private String confirmPw;
    private String name;
}
