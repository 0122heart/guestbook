package com.github.heart0122.guestbook_backend.user.service;

import com.github.heart0122.guestbook_backend.friend.entity.FriendListEntity;
import com.github.heart0122.guestbook_backend.friend.repository.FriendListRepository;
import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookCommentCreateDto;
import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookCommentResponseDto;
import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookDto;
import com.github.heart0122.guestbook_backend.guestbook.entity.CommentEntity;
import com.github.heart0122.guestbook_backend.guestbook.entity.GuestbookEntity;
import com.github.heart0122.guestbook_backend.guestbook.repository.GuestbookRepository;
import com.github.heart0122.guestbook_backend.user.dto.ProfileDto;
import com.github.heart0122.guestbook_backend.user.dto.UserDto;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProfileService {
    private final UserRepository userRepository;
    private final FriendListRepository friendListRepository;
    private final KeepLoginService keepLoginService;
    private final GuestbookRepository guestbookRepository;

    /**
     * 본인 프로필 조회
     * - 본인이므로 모든 정보 + 방명록 포함
     */
    public ProfileDto readMyProfile() {
        String currentNickname = keepLoginService.getNickname();
        UserEntity currentUser = userRepository.findByNickname(currentNickname).orElse(null);

        if (currentUser == null) {
            return null;
        }

        ProfileDto profileDto = new ProfileDto();
        profileDto.setNickname(currentUser.getNickname());
        profileDto.setStatusMsg(currentUser.getStatusMsg());
        profileDto.setRelationId(null); // 본인이므로 null

        // 방명록 설정
        List<GuestbookDto> guestbooks = buildGuestbookDtoList(currentUser);
        profileDto.setGuestbooks(guestbooks);

        return profileDto;
    }

    /**
     * 다른 사용자 프로필 조회
     * - 친구: 모든 정보 + 방명록 포함
     * - 비친구: 닉네임, 상태메시지만 포함
     */
    public ProfileDto readProfile(String nickname) {
        UserEntity targetUser = userRepository.findByNickname(nickname).orElse(null);
        if (targetUser == null) {
            return null;
        }

        ProfileDto profileDto = new ProfileDto();
        profileDto.setId(targetUser.getUserId());
        profileDto.setNickname(nickname);
        profileDto.setStatusMsg(targetUser.getStatusMsg());

        String currentNickname = keepLoginService.getNickname();
        UserEntity currentUser = userRepository.findByNickname(currentNickname).orElse(null);
        FriendListEntity friendListEntity = friendListRepository.findByUserAndFriend(currentUser, targetUser).orElse(null);

        if (friendListEntity == null) {
            // 친구가 아닌 경우: 기본 정보만
            profileDto.setRelationId(null);
            profileDto.setGuestbooks(null);
        } else {
            // 친구인 경우: 전체 정보 + 방명록
            profileDto.setRelationId(friendListEntity.getRelationId());
            List<GuestbookDto> guestbooks = buildGuestbookDtoList(targetUser);
            profileDto.setGuestbooks(guestbooks);
        }

        return profileDto;
    }

    /**
     * 사용자의 방명록 리스트를 DTO로 변환
     * @param user 방명록 주인
     * @return 방명록 DTO 리스트
     */
    private List<GuestbookDto> buildGuestbookDtoList(UserEntity user) {
        List<GuestbookEntity> guestbookEntityList = guestbookRepository.findByOwner(user);
        List<GuestbookDto> guestbookDtoList = new ArrayList<>();

        for (var guestbookEntity : guestbookEntityList) {
            List<GuestbookCommentResponseDto> commentDtoList = buildCommentDtoList(guestbookEntity.getComments());

            GuestbookDto guestbookDto = GuestbookDto.builder()
                    .id(guestbookEntity.getGuestbookId())
                    .ownerNickname(guestbookEntity.getOwner().getNickname())
                    .guestNickname(guestbookEntity.getGuest().getNickname())
                    .title(guestbookEntity.getTitle())
                    .content(guestbookEntity.getContent())
                    .createdAt(guestbookEntity.getCreatedAt())
                    .comments(commentDtoList)
                    .build();

            guestbookDtoList.add(guestbookDto);
        }

        return guestbookDtoList;
    }

    /**
     * 댓글 엔티티 리스트를 DTO로 변환
     * @param commentEntityList 댓글 엔티티 리스트
     * @return 댓글 DTO 리스트
     */
    private List<GuestbookCommentResponseDto> buildCommentDtoList(List<CommentEntity> commentEntityList) {
        List<GuestbookCommentResponseDto> commentDtoList = new ArrayList<>();

        for (var commentEntity : commentEntityList) {
            UserDto userDto = new UserDto();
            userDto.setId(commentEntity.getUser().getUserId());
            userDto.setNickname(commentEntity.getUser().getNickname());
            GuestbookCommentResponseDto commentDto = GuestbookCommentResponseDto.builder()
                    .user(userDto)
                    .commentId(commentEntity.getCommentId())
                    .content(commentEntity.getContent())
                    .createdAt(commentEntity.getCreatedAt())
                    .build();
            commentDtoList.add(commentDto);
        }

        return commentDtoList;
    }
}