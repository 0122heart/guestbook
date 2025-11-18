package com.github.heart0122.guestbook_backend.user.controller;

import com.github.heart0122.guestbook_backend.user.dto.SignUpDto;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.service.SignUpService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Data
public class SignUpController {
    private final SignUpService signUpService;

    @GetMapping("/sign/up")
    public ResponseEntity<String> signUp(SignUpDto signUpDto) {
        if(signUpService.signUp(signUpDto)) return new ResponseEntity<>("SUCCESS!", HttpStatus.OK);
        else return new ResponseEntity<>("FAIL", HttpStatus.BAD_REQUEST);
    }
}
