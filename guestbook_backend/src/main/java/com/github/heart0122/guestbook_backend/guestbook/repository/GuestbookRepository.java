package com.github.heart0122.guestbook_backend.guestbook.repository;

import com.github.heart0122.guestbook_backend.guestbook.entity.GuestbookEntity;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestbookRepository extends JpaRepository<GuestbookEntity, Long> {
    List<GuestbookEntity> findByOwner(UserEntity userEntity);

    @Query("SELECT g FROM GuestbookEntity g WHERE g.owner IN :owners ORDER BY g.createdAt DESC")
    Page<GuestbookEntity> findByOwnerInOrderByCreatedAtDesc(
            @Param("owners") List<UserEntity> owners,
            Pageable pageable
    );
}
