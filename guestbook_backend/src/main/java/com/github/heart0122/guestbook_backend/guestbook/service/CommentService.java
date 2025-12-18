package com.github.heart0122.guestbook_backend.guestbook.service;

import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookCommentCreateDto;
import com.github.heart0122.guestbook_backend.guestbook.entity.CommentEntity;
import com.github.heart0122.guestbook_backend.guestbook.entity.GuestbookEntity;
import com.github.heart0122.guestbook_backend.guestbook.repository.CommentRepository;
import com.github.heart0122.guestbook_backend.guestbook.repository.GuestbookRepository;
import com.github.heart0122.guestbook_backend.user.dto.UserDto;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import com.github.heart0122.guestbook_backend.user.service.KeepLoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentService {
    private final CommentRepository commentRepository;
    private final GuestbookRepository guestbookRepository;
    private final UserRepository userRepository;
    private final KeepLoginService keepLoginService;

    @Transactional
    public void addComment(Long guestbookId, GuestbookCommentCreateDto commentDto) {
        log.info("1. 방명록 조회");
        GuestbookEntity guestbook = guestbookRepository.findById(guestbookId)
                .orElseThrow(() -> new IllegalArgumentException("방명록을 찾을 수 없습니다."));

        log.info("2. 댓글 생성 및 양방향 연관관계 설정");
        UserDto userDto = keepLoginService.getCurrentUser();
        UserEntity userEntity = userRepository.findById(userDto.getId()).orElse(null);
        CommentEntity comment = CommentEntity.builder()
                .guestbook(guestbook)
                .user(userEntity)
                .content(commentDto.getContent())
                .build();

        log.info("3. 양쪽에 모두 설정");
        guestbook.getComments().add(comment);

        log.info("4. 저장");
        commentRepository.save(comment);
    }

    @Transactional
    public void updateComment(Long commentId, GuestbookCommentCreateDto commentDto) {
        // 1. 댓글 조회
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 2. 권한 체크 (작성자만 수정 가능)
        if (comment.getUser().getUserId() != keepLoginService.getId()) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }

        // 3. 내용만 수정 (양쪽 설정 불필요!)
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId) {
        // 1. 댓글 조회
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 2. 양방향 관계 정리
        GuestbookEntity guestbook = comment.getGuestbook();
        guestbook.getComments().remove(comment);

        // 3 삭제
        commentRepository.delete(comment);
    }
}