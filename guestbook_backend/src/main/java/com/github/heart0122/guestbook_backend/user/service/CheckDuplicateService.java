package com.github.heart0122.guestbook_backend.user.service;

import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class CheckDuplicateService {
    private final UserRepository userRepository;

    public boolean checkLoginId(String checkObject) {
        // 아이디가 이미 있으면 중복이니 true반환
        return userRepository.existsByLoginId(checkObject);
    }

    public boolean checkNickname(String checkObject) {
        return userRepository.existsByNickname(checkObject);
    }
}
