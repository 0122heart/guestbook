package com.github.heart0122.guestbook_backend.friend.controller;

import com.github.heart0122.guestbook_backend.friend.service.FriendAcceptanceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/friend")
public class FriendAcceptanceController {
    private FriendAcceptanceService friendAcceptanceService;

    @PostMapping("/{request-id}/{accept}")
    public ResponseEntity<String> acceptFriendRequest(
            @PathVariable("request-id") Long requestId, @PathVariable("accept") boolean accept) {
        if(friendAcceptanceService.acceptOrReject(requestId, accept))
            return new ResponseEntity<>("Accepted", HttpStatus.OK);
        else return new ResponseEntity<>("Rejected", HttpStatus.BAD_REQUEST);
    }
}
