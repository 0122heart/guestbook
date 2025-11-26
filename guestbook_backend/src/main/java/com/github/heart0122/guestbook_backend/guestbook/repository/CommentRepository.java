package com.github.heart0122.guestbook_backend.guestbook.repository;

import com.github.heart0122.guestbook_backend.guestbook.entity.CommentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
}
