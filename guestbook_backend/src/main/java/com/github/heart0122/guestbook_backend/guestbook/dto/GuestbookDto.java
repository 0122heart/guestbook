package com.github.heart0122.guestbook_backend.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestbookDto {
    private Long id;
    private String ownerNickname;
    private String guestNickname;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private List<GuestbookCommentDto> comments;
}
