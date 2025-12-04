package com.github.heart0122.guestbook_backend.friend.service;

import com.github.heart0122.guestbook_backend.friend.dto.FriendListDto;
import com.github.heart0122.guestbook_backend.friend.dto.FriendRequestDto;
import com.github.heart0122.guestbook_backend.friend.entity.FriendListEntity;
import com.github.heart0122.guestbook_backend.friend.entity.FriendRequestEntity;
import com.github.heart0122.guestbook_backend.friend.repository.FriendRepository;
import com.github.heart0122.guestbook_backend.friend.repository.FriendRequestRepository;
import com.github.heart0122.guestbook_backend.user.service.KeepLoginService;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor  // @Data 대신 @RequiredArgsConstructor 사용
public class FriendListService {
    private final FriendRepository friendRepository;
    private final UserRepository userRepository;
    private final KeepLoginService keepLoginService;
    private final FriendRequestRepository friendRequestRepository;

    /**
     * 친구 목록 조회
     * @return 친구 목록 DTO 리스트
     */
    public List<FriendListDto> getFriendList() {
        UserEntity user = userRepository.findById(keepLoginService.getId()).orElse(null);

        if (user == null) {
            return new ArrayList<>();
        }

        List<FriendListEntity> friendList = friendRepository.findByUser(user);
        List<FriendListDto> result = new ArrayList<>();

        for (FriendListEntity f : friendList) {
            FriendListDto dto = FriendListDto.builder()
                    .userId(f.getFriend().getUserId())
                    .nickname(f.getFriend().getNickname())
                    .createdAt(f.getCreatedAt())  // 친구 된 날짜 추가
                    .build();
            result.add(dto);
        }

        return result;
    }

    /**
     * 받은 친구 요청 목록 조회
     * @return 친구 요청 DTO 리스트
     */
    public List<FriendRequestDto> getRequestList() {
        UserEntity user = userRepository.findById(keepLoginService.getId()).orElse(null);

        if (user == null) {
            return new ArrayList<>();
        }

        List<FriendRequestEntity> friendRequestEntityList = friendRequestRepository.findByReceiver(user);
        List<FriendRequestDto> result = new ArrayList<>();

        for (FriendRequestEntity f : friendRequestEntityList) {
            FriendRequestDto friendRequestDto = FriendRequestDto.builder()
                    .requestId(f.getRequestId())
                    .sender(f.getSender().getNickname())
                    .createdAt(f.getCreatedAt())
                    .build();
            result.add(friendRequestDto);
        }

        return result;
    }
}