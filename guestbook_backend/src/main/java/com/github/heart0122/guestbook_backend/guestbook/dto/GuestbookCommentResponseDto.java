package com.github.heart0122.guestbook_backend.guestbook.dto;

import com.github.heart0122.guestbook_backend.user.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GuestbookCommentResponseDto {
    private UserDto user; // Long id, String nickname
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
}
