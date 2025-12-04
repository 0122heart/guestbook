package com.github.heart0122.guestbook_backend.guestbook.service;

import com.github.heart0122.guestbook_backend.friend.entity.FriendListEntity;
import com.github.heart0122.guestbook_backend.friend.repository.FriendRepository;
import com.github.heart0122.guestbook_backend.guestbook.dto.*;
import com.github.heart0122.guestbook_backend.guestbook.entity.GuestbookEntity;
import com.github.heart0122.guestbook_backend.guestbook.repository.GuestbookRepository;
import com.github.heart0122.guestbook_backend.user.dto.UserDto;
import com.github.heart0122.guestbook_backend.user.service.KeepLoginService;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class GuestbookService {
    private final GuestbookRepository guestbookRepository;
    private final UserRepository userRepository;
    private final KeepLoginService keepLoginService;
    private final FriendRepository friendRepository;

    @Transactional
    public boolean post(GuestbookPostDto guestbookPostDto) {
        // 로그 추가 (디버깅용)
        log.info("받은 DTO: {}", guestbookPostDto);
        log.info("ownerId: {}", guestbookPostDto.getOwnerId());

        if (!keepLoginService.isLogin()) {
            log.info("로그인 안 됨");
            return false;
        }

        Long guestId = keepLoginService.getId();
        if (guestId == null) {
            log.info("guestId가 null");
            return false;
        }

        if (guestbookPostDto.getOwnerId() == null) {
            log.info("ownerId가 null");
            return false;
        }

        log.info("Owner 조회 시작 - ID: {}", guestbookPostDto.getOwnerId());

        // ownerId로 owner 찾기
        UserEntity owner = userRepository.findById(guestbookPostDto.getOwnerId())
                .orElse(null);

        log.info("Guest 조회 시작 - ID: {}", guestId);

        // 작성자(guest) 찾기
        UserEntity guest = userRepository.findById(guestId).orElse(null);

        if (owner == null) {
            log.info("owner를 찾을 수 없음");
            return false;
        }

        if (guest == null) {
            log.info("guest를 찾을 수 없음");
            return false;
        }

        log.info("방명록 저장 시작");

        GuestbookEntity guestbook = GuestbookEntity.builder()
                .owner(owner)
                .guest(guest)
                .title(guestbookPostDto.getTitle())
                .content(guestbookPostDto.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        guestbookRepository.save(guestbook);
        log.info("방명록 저장 완료");
        return true;
    }

    @Transactional
    public boolean patch(Long guestbookId, GuestbookPatchDto guestbookPatchDto) {
        GuestbookEntity guestbook = guestbookRepository.findById(guestbookId)
                .orElse(null);

        if (guestbook == null) {
            return false;
        }

        guestbook.setTitle(guestbookPatchDto.getTitle());
        guestbook.setContent(guestbookPatchDto.getContent());
        guestbookRepository.save(guestbook);

        return true;
    }

    @Transactional
    public boolean delete(Long guestbookId) {
        try {
            guestbookRepository.deleteById(guestbookId);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<GuestbookDto> read(String userNickname) {
        UserEntity userEntity = userRepository.findByNickname(userNickname)
                .orElse(null);

        if (userEntity == null) {
            return new ArrayList<>();
        }

        List<GuestbookDto> guestbookDtos = new ArrayList<>();
        List<GuestbookEntity> guestbooks = guestbookRepository.findByOwner(userEntity);

        for (GuestbookEntity guestbook : guestbooks) {
            GuestbookDto guestbookDto = GuestbookDto.builder()
                    .id(guestbook.getGuestbookId())
                    .ownerNickname(userEntity.getNickname())
                    .guestNickname(guestbook.getGuest().getNickname())
                    .title(guestbook.getTitle())
                    .content(guestbook.getContent())
                    .createdAt(guestbook.getCreatedAt())
                    .comments(new ArrayList<>())
                    .build();

            // 댓글 추가
            for (var comment : guestbook.getComments()) {
                UserDto userDto = new UserDto();
                userDto.setId(comment.getUser().getUserId());
                userDto.setNickname(comment.getUser().getNickname());
                GuestbookCommentResponseDto commentDto = GuestbookCommentResponseDto.builder()
                        .user(userDto)
                        .commentId(comment.getCommentId())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build();
                guestbookDto.getComments().add(commentDto);
            }

            guestbookDtos.add(guestbookDto);
        }

        return guestbookDtos;
    }

    public Page<GuestbookDto> getFriendsGuestbookFeed(int page, int size) {
        UserEntity user = userRepository.findById(keepLoginService.getId()).orElse(null);
        if (user == null) return null;

        List<FriendListEntity> friendList = friendRepository.findByUser(user);
        List<UserEntity> friend = new ArrayList<>();
        for (FriendListEntity friendListEntity : friendList) {
            friend.add(friendListEntity.getFriend());
        }

        // 페이지 설정
        Pageable pageable = PageRequest.of(page, size);

        // 친구들의 방명록 조회
        Page<GuestbookEntity> guestbooks = guestbookRepository.findByOwnerInOrderByCreatedAtDesc(friend, pageable);

        // DTO로 변환
        return guestbooks.map(this::convertToDto);
    }

    private GuestbookDto convertToDto(GuestbookEntity entity) {
        return GuestbookDto.builder()
                .id(entity.getGuestbookId())
                .ownerNickname(entity.getOwner().getNickname())
                .guestNickname(entity.getGuest().getNickname())
                .title(entity.getTitle())
                .content(entity.getContent())
                .createdAt(entity.getCreatedAt())
                .build();
    }
}