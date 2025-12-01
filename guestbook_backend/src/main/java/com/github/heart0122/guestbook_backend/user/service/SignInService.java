package com.github.heart0122.guestbook_backend.user.service;

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
    private final KeepLoginService keepLoginService;

    public boolean signIn(SignInDto signInDto) {
        log.info("=== 로그인 시도 시작 ===");
        log.info("SignInDto: {}", signInDto);

        // 1단계: 아이디 검증
        if(signInDto.getLoginId() == null || signInDto.getLoginId().isEmpty()) {
            log.error("실패: 아이디가 null 또는 비어있음");
            return false;
        }
        log.info("1단계 통과: 아이디 존재");

        // 2단계: 사용자 조회
        log.info("DB에서 사용자 조회 중: {}", signInDto.getLoginId());
        Optional<UserEntity> userOpt = userRepository.findByLoginId(signInDto.getLoginId());
        UserEntity user = userOpt.orElse(null);

        if (user == null) {
            log.error("실패: 사용자를 찾을 수 없음");
            return false;
        }
        log.info("2단계 통과: 사용자 찾음 - userId: {}, nickname: {}",
                user.getUserId(), user.getNickname());

        // 3단계: 비밀번호 검증
        log.info("비밀번호 검증 중");
        log.info("입력된 비밀번호: [{}]", signInDto.getPassword());
        log.info("DB 해시값: [{}]", user.getPassword());

        boolean passwordMatch = passwordEncoder.matches(signInDto.getPassword(), user.getPassword());
        log.info("비밀번호 일치 여부: {}", passwordMatch);

        if (!passwordMatch) {
            log.error("실패: 비밀번호 불일치");
            return false;
        }
        log.info("3단계 통과: 비밀번호 일치");

        // 4단계: 세션에 저장
        log.info("KeepLoginComponent에 저장 시도");
        keepLoginService.setId(user.getUserId());
        keepLoginService.setNickname(user.getNickname());

        log.info("저장 완료 - ID: {}, Nickname: {}",
                keepLoginService.getId(), keepLoginService.getNickname());

        log.info("=== 로그인 성공 ===");
        return true;
    }

    public boolean signOut(){
        keepLoginService.setId(null);
        keepLoginService.setNickname(null);
        return true;
    }
}
