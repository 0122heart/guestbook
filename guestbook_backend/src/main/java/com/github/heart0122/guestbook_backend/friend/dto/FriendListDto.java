package com.github.heart0122.guestbook_backend.friend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class FriendListDto {
    private Long userId;
    private String nickname;
    private LocalDateTime createdAt;
}