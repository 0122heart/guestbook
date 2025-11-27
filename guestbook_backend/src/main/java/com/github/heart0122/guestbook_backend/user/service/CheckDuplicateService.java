package com.github.heart0122.guestbook_backend.user.service;

import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CheckDuplicateService {
    private final UserRepository userRepository;

    public boolean checkLoginId(String checkObject) {
        // 결과가 없으면(null이면) unique하므로 true 반환
        return userRepository.findByLoginId(checkObject).orElse(null) == null;
    }

    public boolean checkNickname(String checkObject) {
        // 결과가 없으면(null이면) unique하므로 true 반환
        return userRepository.findByNickname(checkObject).orElse(null) == null;
    }
}
