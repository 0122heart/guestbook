package com.github.heart0122.guestbook_backend.user.dto;

import com.github.heart0122.guestbook_backend.user.dto.SignInDto;
import lombok.Data;

@Data
public class SignUpDto {
    // 아이디 비밀번호
    private SignInDto signInDto;

    // 인증번호
    private String certificationNumber;

    // 이메일
    private String email;

    // 상태메세지
    private String statusMessage;
}
