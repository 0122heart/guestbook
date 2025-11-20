package com.github.heart0122.guestbook_backend.guestbook.dto;

import lombok.Data;

@Data
public class GuestbookPostDto {
    private String ownerNickname;
    private String guestNickname;
    private String title;
    private String content;
}
