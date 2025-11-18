package com.github.heart0122.guestbook_backend.user.dto;

import lombok.Data;

@Data
public class SignInDto {
    // 아이디
    private String loginId;

    // 비밀번호
    private String password;
}
