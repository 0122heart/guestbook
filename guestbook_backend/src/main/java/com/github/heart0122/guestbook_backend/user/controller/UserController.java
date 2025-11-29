package com.github.heart0122.guestbook_backend.user.controller;

import com.github.heart0122.guestbook_backend.user.KeepLoginComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class UserController {
    private final KeepLoginComponent keepLoginComponent;

    @GetMapping("/current")
    public ResponseEntity<?> getCurrentUser() {
        log.info("========== /api/current 호출 ==========");
        log.info("keepLoginComponent hashCode: {}", System.identityHashCode(keepLoginComponent));
        log.info("keepLoginComponent.getId(): {}", keepLoginComponent.getId());
        log.info("keepLoginComponent.getNickname(): {}", keepLoginComponent.getNickname());
        log.info("keepLoginComponent.isLogin(): {}", keepLoginComponent.isLogin());

        Map<String, Object> currentUser = keepLoginComponent.getCurrentUser();
        log.info("getCurrentUser 결과: {}", currentUser);

        if (currentUser == null) {
            log.error("❌ 세션에 사용자 정보 없음!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Not logged in");
        }

        log.info("✅ 사용자 정보 반환 성공");
        log.info("========================================");
        return ResponseEntity.ok(currentUser);
    }


}
