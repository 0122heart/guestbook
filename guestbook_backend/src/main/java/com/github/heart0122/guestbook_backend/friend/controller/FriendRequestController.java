package com.github.heart0122.guestbook_backend.friend.controller;

import com.github.heart0122.guestbook_backend.friend.dto.FriendDto;
import com.github.heart0122.guestbook_backend.friend.service.FriendRequestService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
@RequestMapping("/friend")
public class FriendRequestController {
    private final FriendRequestService friendRequestService;

    @PostMapping("/request")
    public ResponseEntity<String> friendRequest(FriendDto friendDto) {
        if(friendRequestService.request(friendDto)) return new ResponseEntity<>("Request accepted", HttpStatus.OK);
        else return new ResponseEntity<>("Request rejected", HttpStatus.BAD_REQUEST);
    }
}
