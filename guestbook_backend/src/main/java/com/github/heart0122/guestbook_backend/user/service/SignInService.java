package com.github.heart0122.guestbook_backend.user.service;

import com.github.heart0122.guestbook_backend.user.dto.SignInDto;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Data
@Service
public class SignInService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean signIn(SignInDto signInDto) {
        // 아이디가 null이 아닌지 확인
        if(signInDto.getLoginId() == null || signInDto.getLoginId().isEmpty()) return false;

        // 사용자가 없거나 비밀번호가 일치하지 않는 경우
        UserEntity user = userRepository.findByLoginId(signInDto.getLoginId());
        if (user == null || !passwordEncoder.matches(signInDto.getPassword(), user.getPassword())) {
            return false;
        }
        return true;
    }
}
