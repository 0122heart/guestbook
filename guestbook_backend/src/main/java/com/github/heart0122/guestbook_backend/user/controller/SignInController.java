package com.github.heart0122.guestbook_backend.user.controller;

import com.github.heart0122.guestbook_backend.user.dto.SignInDto;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.service.SignInService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequestMapping("/api/sign")
public class SignInController {
    private final SignInService signInService;

    @PostMapping("/in")
    public ResponseEntity<?> SignIn(@RequestBody SignInDto signInDto){
        // SignInService에 입력된 아이디, 비밀번호 보내서 일치하면 OK
        if(signInService.signIn(signInDto)) return new ResponseEntity<>("Login Success", HttpStatus.OK);
        else return new ResponseEntity<>("Login Failed", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/out")
    public ResponseEntity<?> SignOut(){
        if(signInService.signOut()) return new ResponseEntity<>("Logout Success", HttpStatus.OK);
        else return new ResponseEntity<>("Logout Failed", HttpStatus.BAD_REQUEST);
    }
}
