package com.github.heart0122.guestbook_backend.user;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Data
@Table(name = "users")
public class UserEntity {
    // 유저 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userNumber;

    // 유저 아이디(unique, not null)
    @Column(unique = true, nullable = false, length = 20)
    private String userId;

    // 유저 비밀번호(not null)
    @Column(nullable = false)
    private String userName;

    @Column(nullable = true)
    private String statusMessage;

    

}
