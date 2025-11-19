package com.github.heart0122.guestbook_backend.friend.dto;

import lombok.Data;

@Data
public class FriendDto {
    // 친구 요청 한 사람(의 닉네임)
    private String sender;

    // 친구 요청 받은 사람(의 닉네임)
    private String receiver;
}
