package com.github.heart0122.guestbook_backend.guestbook.controller;

import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookListDto;
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

    @GetMapping("/{user-nickname}")
    public ResponseEntity<List<GuestbookListDto>> readGuestbook(@PathVariable("userNickname") String userNickname) {
        List<GuestbookListDto> guestbookListDtos = guestbookService.read(userNickname);
        return new ResponseEntity<>(guestbookListDtos, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<String> postGuestbook(GuestbookPostDto guestbookPostDto) {
        if(guestbookService.post(guestbookPostDto)) return new ResponseEntity<>("Post successful", HttpStatus.OK);
        else return new ResponseEntity<>("Post failed", HttpStatus.BAD_REQUEST);
    }

    @PatchMapping
    public ResponseEntity<String> patchGuestbook(GuestbookPatchDto guestbookPatchDto) {
        if(guestbookService.patch(guestbookPatchDto)) return new ResponseEntity<>("Patch successful", HttpStatus.OK);
        else return new ResponseEntity<>("Patch failed", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping
    public ResponseEntity<String> deleteGuestbook(GuestbookPatchDto guestbookPatchDto) {
        if(guestbookService.delete(guestbookPatchDto)) return new ResponseEntity<>("Delete successful", HttpStatus.OK);
        else return new ResponseEntity<>("Delete failed", HttpStatus.BAD_REQUEST);
    }
}
