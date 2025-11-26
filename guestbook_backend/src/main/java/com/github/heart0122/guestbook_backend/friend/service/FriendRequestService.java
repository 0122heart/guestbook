package com.github.heart0122.guestbook_backend.friend.service;

import com.github.heart0122.guestbook_backend.friend.dto.FriendDto;
import com.github.heart0122.guestbook_backend.friend.entity.FriendRequestEntity;
import com.github.heart0122.guestbook_backend.friend.repository.FriendRequestRepository;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Data
@Transactional
public class FriendRequestService {
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    public boolean request(String sender, String receiver){
        UserEntity user1 = userRepository.findByNickname(sender);
        UserEntity user2 = userRepository.findByNickname(receiver);
        if(user1 == null || user2 == null) return false;

        FriendRequestEntity friendRequestEntity = FriendRequestEntity.builder().
                sender(user1).
                receiver(user2).
                build();
        friendRequestRepository.save(friendRequestEntity);
        return true;
    }
}
