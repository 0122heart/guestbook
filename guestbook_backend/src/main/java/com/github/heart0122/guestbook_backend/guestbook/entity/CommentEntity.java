package com.github.heart0122.guestbook_backend.guestbook.entity;

import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "comments")
@EntityListeners(AuditingEntityListener.class) // created_at 자동화를 위해
public class CommentEntity {
    // 댓글 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    // 글 번호
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guestbook_id", nullable = false)
    private GuestbookEntity guestbook;

    // 댓글 작성자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    // 댓글 내용
    @Column(columnDefinition = "TEXT", name = "comment", nullable = false)
    private String content;

    // 댓글 작성 시간
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
