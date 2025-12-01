package com.github.heart0122.guestbook_backend.friend.controller;

import com.github.heart0122.guestbook_backend.friend.service.FriendAcceptanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/friend")
@RequiredArgsConstructor
public class FriendAcceptanceController {
    private final FriendAcceptanceService friendAcceptanceService;

    @GetMapping("/{request-id}/{accept}")
    public ResponseEntity<String> acceptFriendRequest(
            @PathVariable("request-id") Long requestId, @PathVariable("accept") boolean accept) {
        if(friendAcceptanceService.acceptOrReject(requestId, accept))
            return new ResponseEntity<>("Accepted", HttpStatus.OK);
        else return new ResponseEntity<>("Rejected", HttpStatus.BAD_REQUEST);
    }
}
