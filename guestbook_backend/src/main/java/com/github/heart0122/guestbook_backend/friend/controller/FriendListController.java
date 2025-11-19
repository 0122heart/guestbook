package com.github.heart0122.guestbook_backend.friend.controller;

import com.github.heart0122.guestbook_backend.friend.entity.FriendListEntity;
import com.github.heart0122.guestbook_backend.friend.service.FriendListService;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/friend")
public class FriendListController {
    private FriendListService friendListService;

    // user nickname으로
    @GetMapping("/list/{nickname}")
    public ResponseEntity<List<Pair<Long, String>>> getFriendList(@PathVariable("nickname") String nickname) {
        List<Pair<Long, String>> friendList = friendListService.getFriendList(nickname);
        return new ResponseEntity<>(friendList, HttpStatus.OK);
    }

}
