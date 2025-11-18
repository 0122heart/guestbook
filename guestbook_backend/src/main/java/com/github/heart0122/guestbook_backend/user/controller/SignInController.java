package com.github.heart0122.guestbook_backend.user.controller;

import com.github.heart0122.guestbook_backend.user.dto.SignInDto;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.service.SignInService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController("/user")
public class SignInController {
    private final SignInService signInService;

    @GetMapping("/sign/in")
    public ResponseEntity<UserEntity> SignIn(@RequestBody SignInDto signInDto){
        // SignInService에 입력된 아이디, 비밀번호 보내서 일치하면 OK
        if(signInService.signIn(signInDto)) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }
}
