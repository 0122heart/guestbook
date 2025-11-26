package com.github.heart0122.guestbook_backend.guestbook.controller;

import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookCommentDto;
import com.github.heart0122.guestbook_backend.guestbook.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/guestbook")
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/{guestbook-id}/comment")
    public ResponseEntity<String> postComment(
            @PathVariable("guestbook-id") Long guestbookId, @RequestBody GuestbookCommentDto commentDto){
        commentService.addComment(guestbookId, commentDto);
        return new ResponseEntity<>("Comment successful", HttpStatus.OK);
    }

    @PatchMapping("/comment/{comment-id}")
    public ResponseEntity<String> updateComment(
            @PathVariable("comment-id") Long commentId, @RequestBody GuestbookCommentDto commentDto){
        commentService.updateComment(commentId, commentDto);
        return new ResponseEntity<>("Comment successful", HttpStatus.OK);
    }

    @DeleteMapping("/comment/{comment-id}")
    public ResponseEntity<String> deleteComment(@PathVariable("comment-id") Long commentId){
        commentService.deleteComment(commentId);
        return new ResponseEntity<>("Comment delete successful", HttpStatus.OK);
    }
}
