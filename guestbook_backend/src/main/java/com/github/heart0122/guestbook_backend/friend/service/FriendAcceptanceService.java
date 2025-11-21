package com.github.heart0122.guestbook_backend.friend.service;

import com.github.heart0122.guestbook_backend.friend.dto.FriendDto;
import com.github.heart0122.guestbook_backend.friend.entity.FriendListEntity;
import com.github.heart0122.guestbook_backend.friend.entity.FriendRequestEntity;
import com.github.heart0122.guestbook_backend.friend.repository.FriendListRepository;
import com.github.heart0122.guestbook_backend.friend.repository.FriendRequestRepository;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Data
@Service
@Transactional
public class FriendAcceptanceService {
    private FriendRequestRepository friendRequestRepository;
    private FriendListRepository friendListRepository;
    private UserRepository userRepository;

    public boolean acceptOrReject(boolean accept, FriendDto friendDto) {
        UserEntity sender = userRepository.findByNickname(friendDto.getSender());
        UserEntity receiver = userRepository.findByNickname(friendDto.getReceiver());
        FriendRequestEntity request = friendRequestRepository.findFriendRequestIdBySenderAndReceiver(sender, receiver);
        Long requestId = request.getRequestId();

        // 친구 요청을 승인하든 거절하든 요청 객체는 삭제됨
        friendRequestRepository.deleteFriendRequestByRequestId(requestId);

        if (accept) {
            UserEntity user1 = userRepository.findByNickname(friendDto.getSender());
            UserEntity user2 = userRepository.findByNickname(friendDto.getReceiver());

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
