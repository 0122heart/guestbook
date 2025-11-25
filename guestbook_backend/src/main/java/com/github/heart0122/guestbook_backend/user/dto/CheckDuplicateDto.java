package com.github.heart0122.guestbook_backend.user.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckDuplicateDto {
    String checkObject;
    boolean isDuplicate = true;
}
