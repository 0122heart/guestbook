package com.github.heart0122.guestbook_backend.user.controller;

import com.github.heart0122.guestbook_backend.user.dto.CheckDuplicateDto;
import com.github.heart0122.guestbook_backend.user.service.CheckDuplicateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sign-up/duplicate-check")
public class CheckDuplicateController {
    private final CheckDuplicateService duplicateCheckService;

    @PostMapping("/login-id")
    public ResponseEntity<CheckDuplicateDto> checkLoginId(@RequestBody CheckDuplicateDto checkDuplicateDto) {
        checkDuplicateDto.setDuplicate(duplicateCheckService.checkNickname(checkDuplicateDto.getCheckObject()));
        return ResponseEntity.ok(checkDuplicateDto);
    }

    @PostMapping("/nickname")
    public ResponseEntity<CheckDuplicateDto> checkNickname(@RequestBody CheckDuplicateDto checkDuplicateDto) {
        checkDuplicateDto.setDuplicate(duplicateCheckService.checkNickname(checkDuplicateDto.getCheckObject()));
        return ResponseEntity.ok(checkDuplicateDto);
    }
}
