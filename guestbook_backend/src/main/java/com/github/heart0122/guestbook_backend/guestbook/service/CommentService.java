package com.github.heart0122.guestbook_backend.guestbook.service;

import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookCommentDto;
import com.github.heart0122.guestbook_backend.guestbook.entity.CommentEntity;
import com.github.heart0122.guestbook_backend.guestbook.entity.GuestbookEntity;
import com.github.heart0122.guestbook_backend.guestbook.repository.CommentRepository;
import com.github.heart0122.guestbook_backend.guestbook.repository.GuestbookRepository;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final GuestbookRepository guestbookRepository;
    private final UserRepository userRepository;

    public void addComment(Long guestbookId, GuestbookCommentDto commentDto) {
        // 1. 방명록 조회
        GuestbookEntity guestbook = guestbookRepository.findById(guestbookId)
                .orElseThrow(() -> new IllegalArgumentException("방명록을 찾을 수 없습니다."));

        // 2. 작성자 조회 (commentDto에 userId가 있다고 가정)
        Optional<UserEntity> userOpt = userRepository.findByNickname(commentDto.getNickname());
        UserEntity user = userOpt.orElse(null);

        // 3. 댓글 생성 및 양방향 연관관계 설정
        CommentEntity comment = CommentEntity.builder()
                .guestbook(guestbook)
                .user(user)
                .content(commentDto.getContent())
                .build();

        // 4. 양쪽에 모두 설정 (중요!)
        guestbook.getComments().add(comment);

        // 5. 저장
        commentRepository.save(comment);
    }

    public void updateComment(Long commentId, GuestbookCommentDto commentDto) {
        // 1. 댓글 조회
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 2. 권한 체크 (작성자만 수정 가능)
        if (!comment.getUser().getNickname().equals(commentDto.getNickname())) {
            throw new IllegalArgumentException("댓글 수정 권한이 없습니다.");
        }

        // 3. 내용만 수정 (양쪽 설정 불필요!)
        comment.setContent(commentDto.getContent());
        commentRepository.save(comment);
    }

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