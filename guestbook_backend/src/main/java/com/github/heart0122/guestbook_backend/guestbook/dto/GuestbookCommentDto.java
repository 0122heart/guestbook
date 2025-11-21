package com.github.heart0122.guestbook_backend.guestbook.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GuestbookCommentDto {
    private Long commentId;
    private String userNickname;
    private String content;
    private LocalDateTime createdAt;
}
