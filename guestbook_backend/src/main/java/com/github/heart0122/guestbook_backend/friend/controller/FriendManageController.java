package com.github.heart0122.guestbook_backend.friend.controller;

import com.github.heart0122.guestbook_backend.friend.service.FriendManageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

@RestController
@RequestMapping("/api/friend")
@RequiredArgsConstructor
public class FriendManageController {
    private final FriendManageService friendManageService;

    @GetMapping("/{request-id}/{accept}")
    public ResponseEntity<String> acceptFriendRequest(
            @PathVariable("request-id") Long requestId, @PathVariable("accept") boolean accept) {
        if(friendManageService.acceptOrReject(requestId, accept))
            return new ResponseEntity<>("Accepted", HttpStatus.OK);
        else return new ResponseEntity<>("Rejected", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{friend-id}")
    public ResponseEntity<String> deleteFriend(@PathVariable("friend-id") Long friendId) {
        if(friendManageService.deleteFriend(friendId)) return new ResponseEntity<>("Deleted", HttpStatus.OK);
        else return new ResponseEntity<>("Delete Fail", HttpStatus.BAD_REQUEST);
    }
}
