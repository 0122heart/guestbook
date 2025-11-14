package com.github.heart0122.guestbook_backend.user;

import com.github.heart0122.guestbook_backend.user.dto.SignInDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/user")
public class SignInController {


    @GetMapping("/signin")
    public ResponseEntity<?> SignIn(@RequestBody SignInDto signInDto){
        // SignInService에 입력된 아이디, 비밀번호 보내서 일치하면 OK

    }
}
