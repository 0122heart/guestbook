package com.github.heart0122.guestbook_backend.user.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Table(name = "users")
public class UserEntity {
    // 회원 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    // 회원 아이디(unique, not null)
    @Column(name = "login_id", unique = true, nullable = false, length = 20)
    private String loginId;

    // 회원 비밀번호(not null)
    @Column(name = "password",nullable = false)
    private String password;

    // 회원 닉네임
    @Column(name = "nickname",unique = true, nullable = true, length = 10)
    private String nickname;

    // 회원 상태메세지
    @Column(name = "status_msg",nullable = true)
    private String statusMsg;
}
