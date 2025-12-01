package com.github.heart0122.guestbook_backend.friend.controller;

import com.github.heart0122.guestbook_backend.friend.dto.FriendListDto;
import com.github.heart0122.guestbook_backend.friend.dto.FriendRequestDto;
import com.github.heart0122.guestbook_backend.friend.service.FriendListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/friend")
@RequiredArgsConstructor
public class FriendListController {
    private final FriendListService friendListService;

    /**
     * 친구 목록 조회
     */
    @GetMapping
    public ResponseEntity<List<FriendListDto>> getFriendList() {
        List<FriendListDto> friendList = friendListService.getFriendList();
        return new ResponseEntity<>(friendList, HttpStatus.OK);
    }

    /**
     * 받은 친구 요청 목록 조회
     */
    @GetMapping("/request")
    public ResponseEntity<List<FriendRequestDto>> getRequestList() {
        return ResponseEntity.ok(friendListService.getRequestList());
    }
}