package com.github.heart0122.guestbook_backend.friend.repository;

import com.github.heart0122.guestbook_backend.friend.entity.FriendListEntity;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendListRepository extends JpaRepository<FriendListEntity, Long> {
    List<FriendListEntity> findByUser(UserEntity user);
    Optional<FriendListEntity> findByUserAndFriend(UserEntity user, UserEntity friend);
}
