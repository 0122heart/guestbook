package com.github.heart0122.guestbook_backend.user.service;

import com.github.heart0122.guestbook_backend.user.KeepLoginComponent;
import com.github.heart0122.guestbook_backend.user.dto.SignInDto;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Data
@Service
@Slf4j
public class SignInService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final KeepLoginComponent keepLoginComponent;

    public boolean signIn(SignInDto signInDto) {
        log.info("SignInDto: {}", signInDto);
        // 아이디가 null이 아닌지 확인
        if(signInDto.getLoginId() == null || signInDto.getLoginId().isEmpty()) return false;

        // 사용자가 없거나 비밀번호가 일치하지 않는 경우
        Optional<UserEntity> userOpt = userRepository.findByLoginId(signInDto.getLoginId());
        UserEntity user = userOpt.orElse(null);
        if (user == null || !passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            return false;
        }
        keepLoginComponent.setId(user.getUserId());
        keepLoginComponent.setNickname(user.getNickname());
        return true;
    }

    public boolean signOut(){
        keepLoginComponent.setId(null);
        keepLoginComponent.setNickname(null);
        return true;
    }
}
