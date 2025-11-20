package com.github.heart0122.guestbook_backend.guestbook.controller;

import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookPatchDto;
import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookPostDto;
import com.github.heart0122.guestbook_backend.guestbook.service.GuestbookService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Data
@RestController
@RequestMapping("/guestbook")
public class GuestbookController {
    private final GuestbookService guestbookService;

    @PostMapping("/post")
    public ResponseEntity<String> postGuestbook(GuestbookPostDto guestbookPostDto) {
        if(guestbookService.post(guestbookPostDto)) return new ResponseEntity<>("Post successful", HttpStatus.OK);
        else return new ResponseEntity<>("Post failed", HttpStatus.BAD_REQUEST);
    }

    @PatchMapping("/patch")
    public ResponseEntity<String> patchGuestbook(GuestbookPatchDto guestbookPatchDto) {
        if(guestbookService.patch(guestbookPatchDto)) return new ResponseEntity<>("Patch successful", HttpStatus.OK);
        else return new ResponseEntity<>("Patch failed", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteGuestbook(GuestbookPatchDto guestbookPatchDto) {
        if(guestbookService.delete(guestbookPatchDto)) return new ResponseEntity<>("Delete successful", HttpStatus.OK);
        else return new ResponseEntity<>("Delete failed", HttpStatus.BAD_REQUEST);
    }
}
