package com.github.heart0122.guestbook_backend.user.service;

import com.github.heart0122.guestbook_backend.user.dto.SignUpDto;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Data
@Service
public class SignUpService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public boolean signUp(SignUpDto signUpDto) {
        // 아이디, 이메일, 닉네임이 null이 아닌지 확인
        if(signUpDto.getLoginId() == null ||
            signUpDto.getNickname() == null) return false;

        // 아이디가 중복되지는 않았는지 확인
        if(userRepository.findByLoginId(signUpDto.getLoginId()).orElse(null) != null) return false;

        // 닉네임이 중복되지는 않았는지 확인
        if(userRepository.findByNickname(signUpDto.getNickname()).orElse(null) != null) return false;

        // DTO -> Entity 변환
        UserEntity userEntity = UserEntity.builder() // UserEntity에 @Builder가 있다고 가정
                .loginId(signUpDto.getLoginId())
                .password(passwordEncoder.encode(signUpDto.getPassword())) // 암호화
                .nickname(signUpDto.getNickname())
                .statusMsg(signUpDto.getStatusMsg())
                .build();
        userRepository.save(userEntity);
        return true;
    }
}
