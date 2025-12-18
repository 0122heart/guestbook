package com.github.heart0122.guestbook_backend.friend.service;

import com.github.heart0122.guestbook_backend.friend.entity.FriendRequestEntity;
import com.github.heart0122.guestbook_backend.friend.repository.FriendRequestRepository;
import com.github.heart0122.guestbook_backend.user.service.KeepLoginService;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Data
@Transactional
public class FriendRequestService {
    private final KeepLoginService keepLoginService;
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Transactional
    public boolean request(Long receiverId){
        if(!keepLoginService.isLogin()) return false;

        Long senderId = keepLoginService.getId();
        Optional<UserEntity> senderOpt = userRepository.findById(senderId);
        Optional<UserEntity> receiverOpt = userRepository.findById(receiverId);
        UserEntity sender = senderOpt.orElse(null);
        UserEntity receiver = receiverOpt.orElse(null);

        if(sender == null || receiver == null) return false;

        // 이미 친구 요청이 있는지 확인 (선택사항)
        if(friendRequestRepository.existsBySenderAndReceiver(sender, receiver)) {
            return false; // 이미 요청을 보냈음
        }

        FriendRequestEntity friendRequestEntity = FriendRequestEntity.builder()
                .sender(sender)
                .receiver(receiver)
                .createdAt(LocalDateTime.now()) // ⭐ 추가!
                .build();

        friendRequestRepository.save(friendRequestEntity);
        return true;
    }
}