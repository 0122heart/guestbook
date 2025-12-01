package com.github.heart0122.guestbook_backend.user.service;

import com.github.heart0122.guestbook_backend.user.dto.UserDto;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserSearchService {
    private UserDto userDto;
    private final UserRepository userRepository;

    public UserDto search(String nickname) {
        UserEntity user = userRepository.findByNickname(nickname).orElse(null);

        if(user == null) return null;

        userDto = new UserDto();
        userDto.setId(user.getUserId());
        userDto.setNickname(nickname);
        return userDto;
    }
}
