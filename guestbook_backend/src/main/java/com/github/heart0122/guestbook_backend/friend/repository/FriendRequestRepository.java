package com.github.heart0122.guestbook_backend.friend.repository;

import com.github.heart0122.guestbook_backend.friend.entity.FriendRequestEntity;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequestEntity, Long> {
    boolean existsBySenderAndReceiver(UserEntity sender, UserEntity receiver);
    List<FriendRequestEntity> findByReceiver(UserEntity receiver);
}
