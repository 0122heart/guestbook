package com.github.heart0122.guestbook_backend.guestbook.repository;

import com.github.heart0122.guestbook_backend.guestbook.entity.GuestbookEntity;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GuestbookRepository extends JpaRepository<GuestbookEntity, Long> {
    void deleteByGuestbookId(Long guestbookId);
    GuestbookEntity save(GuestbookEntity guestbookEntity);
    List<GuestbookEntity> findGuestbookEntitiesByOwner(UserEntity userEntity);
}
