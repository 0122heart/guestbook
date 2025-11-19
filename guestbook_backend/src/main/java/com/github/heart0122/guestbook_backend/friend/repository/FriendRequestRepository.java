package com.github.heart0122.guestbook_backend.friend.repository;

import com.github.heart0122.guestbook_backend.friend.entity.FriendRequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequestEntity, Long> {
    public Long findFriendRequestIdBySenderAndReceiver(String sender, String receiver);
    public void deleteFriendRequestById(Long requestId);
}
