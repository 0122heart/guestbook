package com.github.heart0122.guestbook_backend.user.dto;

import lombok.Data;

@Data
public class SignUpDto {
    // 아이디
    private String loginId;

    // 비밀번호
    private String password;

    // 인증번호
    private String certificationNumber;

    // 이메일
    private String email;

    // 닉네임
    private String nickname;

    // 상태메세지
    private String statusMsg;
}
