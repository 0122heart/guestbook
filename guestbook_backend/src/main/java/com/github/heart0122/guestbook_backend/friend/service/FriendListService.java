package com.github.heart0122.guestbook_backend.friend.service;

import com.github.heart0122.guestbook_backend.friend.entity.FriendListEntity;
import com.github.heart0122.guestbook_backend.friend.repository.FriendListRepository;
import com.github.heart0122.guestbook_backend.user.KeepLoginComponent;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.Data;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class FriendListService {
    private final FriendListRepository friendListRepository;
    private final UserRepository userRepository;
    private final KeepLoginComponent keepLoginComponent;

    public List<Pair<Long, String>> getFriendList(){
        Optional<UserEntity> userOpt = userRepository.findById(keepLoginComponent.getId());
        UserEntity user = userOpt.orElse(null);
        List<FriendListEntity> friendList = friendListRepository.findFriendByUser(user);

        List<Pair<Long, String>> result = new ArrayList<>();
        for(FriendListEntity f : friendList){
            result.add(Pair.of(f.getFriend().getUserId(), f.getFriend().getNickname()));
        }

        return result;
    }
}
