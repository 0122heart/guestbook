package com.github.heart0122.guestbook_backend.guestbook.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class GuestbookListDto {
    private Long id = null;
    private String ownerNickname;
    private String guestNickname;
    private String title;
    private String content;
    private LocalDateTime createdAt = null;
    private List<GuestbookCommentDto> comments;
}
