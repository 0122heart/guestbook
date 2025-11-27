package com.github.heart0122.guestbook_backend.friend.service;

import com.github.heart0122.guestbook_backend.friend.entity.FriendRequestEntity;
import com.github.heart0122.guestbook_backend.friend.repository.FriendRequestRepository;
import com.github.heart0122.guestbook_backend.user.KeepLoginComponent;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Data
@Transactional
public class FriendRequestService {
    private final KeepLoginComponent keepLoginComponent;
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    public boolean request(Long receiverId){
        if(!keepLoginComponent.isLogin()) return false;

        Long senderId = keepLoginComponent.getId();
        Optional<UserEntity> senderOpt = userRepository.findById(senderId);
        Optional<UserEntity> receiverOpt = userRepository.findById(receiverId);
        UserEntity sender = senderOpt.orElse(null);
        UserEntity receiver = receiverOpt.orElse(null);

        if(sender == null || receiver == null) return false;

        FriendRequestEntity friendRequestEntity = FriendRequestEntity.builder().
                sender(sender).
                receiver(receiver).
                build();
        friendRequestRepository.save(friendRequestEntity);
        return true;
    }
}
