package com.github.heart0122.guestbook_backend.guestbook.controller;

import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookDto;
import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookPatchDto;
import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookPostDto;
import com.github.heart0122.guestbook_backend.guestbook.service.GuestbookService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Data
@RestController
@RequestMapping("api/guestbook")
public class GuestbookController {
    private final GuestbookService guestbookService;

    @GetMapping("/{nickname}")
    public ResponseEntity<List<GuestbookDto>> readGuestbook(@PathVariable("nickname") String nickname) {
        List<GuestbookDto> guestbookDtos = guestbookService.read(nickname);
        return new ResponseEntity<>(guestbookDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> postGuestbook(GuestbookPostDto guestbookPostDto) {
        if(guestbookService.post(guestbookPostDto)) return new ResponseEntity<>("Post successful", HttpStatus.OK);
        else return new ResponseEntity<>("Post failed", HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/{guestbook-id}")
    public ResponseEntity<String> patchGuestbook(
            @PathVariable("guestbook-id") Long guestbookId, GuestbookPatchDto guestbookPatchDto) {
        if(guestbookService.patch(guestbookId, guestbookPatchDto))
            return new ResponseEntity<>("Patch successful", HttpStatus.OK);
        else return new ResponseEntity<>("Patch failed", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("{guestbook-id}")
    public ResponseEntity<String> deleteGuestbook(
            @PathVariable("guestbook-id") Long guestbookId) {
        if(guestbookService.delete(guestbookId)) return new ResponseEntity<>("Delete successful", HttpStatus.OK);
        else return new ResponseEntity<>("Delete failed", HttpStatus.BAD_REQUEST);
    }
}
