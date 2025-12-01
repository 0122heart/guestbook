package com.github.heart0122.guestbook_backend.guestbook.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GuestbookPostDto {
    private Long ownerId;
    private String title;
    private String content;
}
