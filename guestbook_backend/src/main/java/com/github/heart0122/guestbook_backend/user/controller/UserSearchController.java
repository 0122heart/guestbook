package com.github.heart0122.guestbook_backend.user.controller;


import com.github.heart0122.guestbook_backend.user.dto.UserDto;
import com.github.heart0122.guestbook_backend.user.service.UserSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserSearchController {
    private final UserSearchService userSearchService;

    @GetMapping("/search/{nickname}")
    public ResponseEntity<?> search(@PathVariable("nickname") String nickname) {
        UserDto userDto = userSearchService.search(nickname);
        if(userDto == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDto);
    }
}
