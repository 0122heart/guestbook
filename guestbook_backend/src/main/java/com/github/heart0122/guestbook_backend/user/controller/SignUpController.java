package com.github.heart0122.guestbook_backend.user.controller;

import com.github.heart0122.guestbook_backend.user.dto.SignUpDto;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.service.SignUpService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@Data
@RequestMapping("/api/sign-up")
public class SignUpController {
    private final SignUpService signUpService;

    @PostMapping
    public ResponseEntity<Map<String, String>> signUp(@RequestBody SignUpDto signUpDto) {
        HashMap<String, String> response = new HashMap<>();
        if(signUpService.signUp(signUpDto)){
            response.put("status", "success");
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            response.put("status", "fail");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

    }
}
