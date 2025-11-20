package com.github.heart0122.guestbook_backend.guestbook.entity;

import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Table(name = "guestbook")
@EntityListeners(AuditingEntityListener.class) // created_at 자동화를 위해
public class GuestbookEntity {
    // 글 번호
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "guestbook_id")
    private Long guestbookId;

    // 방명록 주인
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private UserEntity owner;

    // 방명록에 글 쓰러 온 손님
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guest_id", nullable = false)
    private UserEntity guest;

    // 글 제목
    @Column(name = "title", nullable = false)
    private String title;

    // 글 내용(Long String)
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(mappedBy = "guestbook", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CommentEntity> comments = new ArrayList<>();

    // 글 작성 시간
    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
