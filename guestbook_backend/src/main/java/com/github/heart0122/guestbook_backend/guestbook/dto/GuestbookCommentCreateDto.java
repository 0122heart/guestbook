package com.github.heart0122.guestbook_backend.guestbook.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestbookCommentCreateDto {
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
}
