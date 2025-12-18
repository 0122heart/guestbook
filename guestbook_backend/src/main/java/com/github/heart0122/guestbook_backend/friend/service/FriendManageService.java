package com.github.heart0122.guestbook_backend.friend.service;

import com.github.heart0122.guestbook_backend.friend.entity.FriendListEntity;
import com.github.heart0122.guestbook_backend.friend.entity.FriendRequestEntity;
import com.github.heart0122.guestbook_backend.friend.repository.FriendRepository;
import com.github.heart0122.guestbook_backend.friend.repository.FriendRequestRepository;
import com.github.heart0122.guestbook_backend.user.service.KeepLoginService;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Data
@Service
@Transactional
public class FriendManageService {
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final KeepLoginService keepLoginService;

    @Transactional
    public boolean acceptOrReject(Long requestId, boolean accept) {
        if(!keepLoginService.isLogin()) return false;

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

            friendRepository.save(connection1);
            friendRepository.save(connection2);
        }

        return true;
    }

    @Transactional
    public boolean deleteFriend(Long friendId) {
        if (!keepLoginService.isLogin()) return false;



        UserEntity user1 = userRepository.findById(keepLoginService.getId()).orElse(null);
        UserEntity user2 = userRepository.findById(friendId).orElse(null);

        // 양방향 삭제
        Long result = 0L;
        result += friendRepository.deleteByUserAndFriend(user1, user2);
        result += friendRepository.deleteByUserAndFriend(user2, user1);

        return result == 2L;
    }
}
