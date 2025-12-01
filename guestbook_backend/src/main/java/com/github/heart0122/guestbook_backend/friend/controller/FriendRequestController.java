package com.github.heart0122.guestbook_backend.friend.controller;

import com.github.heart0122.guestbook_backend.friend.service.FriendRequestService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequestMapping("/api/friend")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @PostMapping("/request/{receiver}")
    public ResponseEntity<String> friendRequest(
            @PathVariable("receiver") Long receiver) {
        if(friendRequestService.request(receiver))
            return new ResponseEntity<>("Request accepted", HttpStatus.OK);
        else return new ResponseEntity<>("Request rejected", HttpStatus.BAD_REQUEST);
    }
}
