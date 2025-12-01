package com.github.heart0122.guestbook_backend.user.controller;

import com.github.heart0122.guestbook_backend.user.dto.ProfileDto;
import com.github.heart0122.guestbook_backend.user.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ProfileController {
    private final ProfileService profileService;

    @GetMapping("/profile")
    public ResponseEntity<?> getMyProfile(){
        ProfileDto profileDto = profileService.readMyProfile();
        return ResponseEntity.ok(profileDto);
    }

    @GetMapping("/profile/{nickname}")
    public ResponseEntity<?> getProfile(@PathVariable("nickname") String nickname) {
        ProfileDto profileDto = profileService.readProfile(nickname);
        if(profileDto == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(profileDto);
    }
}
