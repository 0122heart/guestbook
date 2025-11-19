package com.github.heart0122.guestbook_backend.friend.service;

import com.github.heart0122.guestbook_backend.friend.entity.FriendListEntity;
import com.github.heart0122.guestbook_backend.friend.repository.FriendListRepository;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.Data;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
@Transactional
public class FriendListService {
    private final FriendListRepository friendListRepository;
    private final UserRepository userRepository;

    public List<Pair<Long, String>> getFriendList(String nickname){
        UserEntity user = userRepository.findByNickname(nickname);
        List<FriendListEntity> friendList = friendListRepository.findFriendByUser(user);

        List<Pair<Long, String>> result = new ArrayList<>();
        for(FriendListEntity f : friendList){
            result.add(Pair.of(f.getFriend().getUserId(), f.getFriend().getNickname()));
        }

        return result;
    }
}
