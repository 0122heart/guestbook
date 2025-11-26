package com.github.heart0122.guestbook_backend.guestbook.dto;

import lombok.Data;

@Data
public class GuestbookPatchDto {
    private String title;
    private String content;
}
