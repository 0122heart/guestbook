package com.github.heart0122.guestbook_backend.friend.service;

import com.github.heart0122.guestbook_backend.friend.entity.FriendListEntity;
import com.github.heart0122.guestbook_backend.friend.entity.FriendRequestEntity;
import com.github.heart0122.guestbook_backend.friend.repository.FriendListRepository;
import com.github.heart0122.guestbook_backend.friend.repository.FriendRequestRepository;
import com.github.heart0122.guestbook_backend.user.KeepLoginComponent;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Data
@Service
@Transactional
public class FriendAcceptanceService {
    private final FriendRequestRepository friendRequestRepository;
    private final FriendListRepository friendListRepository;
    private final UserRepository userRepository;
    private final KeepLoginComponent keepLoginComponent;

    public boolean acceptOrReject(Long requestId, boolean accept) {
        if(!keepLoginComponent.isLogin()) return false;

        // 친구 요청을 승인하든 거절하든 요청 객체는 삭제됨
        FriendRequestEntity friendRequest = friendRequestRepository.findById(requestId).orElse(null);
        friendRequestRepository.deleteById(requestId);

        if (accept) {
            UserEntity user1 = friendRequest.getSender();
            UserEntity user2 = friendRequest.getReceiver();

            FriendListEntity connection1 = FriendListEntity.builder().
                    user(user1).
                    friend(user2).
                    build();
            FriendListEntity connection2 = FriendListEntity.builder().
                    user(user2).
                    friend(user1).
                    build();

            friendListRepository.save(connection1);
            friendListRepository.save(connection2);
        }

        return true;
    }
}
