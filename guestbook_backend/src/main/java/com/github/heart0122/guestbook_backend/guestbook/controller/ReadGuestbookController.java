package com.github.heart0122.guestbook_backend.guestbook.controller;

import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookListDto;
import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookPostDto;
import com.github.heart0122.guestbook_backend.guestbook.service.GuestbookService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Data
@RestController
@RequestMapping("/guestbook")
public class ReadGuestbookController {
    private final GuestbookService guestbookService;

    @GetMapping("/read/{userNickname}")
    public ResponseEntity<List<GuestbookListDto>> readGuestbook(@PathVariable("userNickname") String userNickname) {
        List<GuestbookListDto> guestbookListDtos = guestbookService.read(userNickname);
        return new ResponseEntity<>(guestbookListDtos, HttpStatus.OK);
    }
}
