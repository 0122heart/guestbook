package com.github.heart0122.guestbook_backend.guestbook.service;

import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookCommentDto;
import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookDto;
import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookPatchDto;
import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookPostDto;
import com.github.heart0122.guestbook_backend.guestbook.entity.GuestbookEntity;
import com.github.heart0122.guestbook_backend.guestbook.repository.GuestbookRepository;
import com.github.heart0122.guestbook_backend.user.service.KeepLoginService;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GuestbookService {
    private final GuestbookRepository guestbookRepository;
    private final UserRepository userRepository;
    private final KeepLoginService keepLoginService;

    @Transactional
    public boolean post(GuestbookPostDto guestbookPostDto) {
        // 로그 추가 (디버깅용)
        System.out.println("받은 DTO: " + guestbookPostDto);
        System.out.println("ownerId: " + guestbookPostDto.getOwnerId());

        if (!keepLoginService.isLogin()) {
            System.out.println("로그인 안 됨");
            return false;
        }

        Long guestId = keepLoginService.getId();
        if (guestId == null) {
            System.out.println("guestId가 null");
            return false;
        }

        if (guestbookPostDto.getOwnerId() == null) {
            System.out.println("ownerId가 null");
            return false;
        }

        System.out.println("Owner 조회 시작 - ID: " + guestbookPostDto.getOwnerId());

        // ownerId로 owner 찾기
        UserEntity owner = userRepository.findById(guestbookPostDto.getOwnerId())
                .orElse(null);

        System.out.println("Guest 조회 시작 - ID: " + guestId);

        // 작성자(guest) 찾기
        UserEntity guest = userRepository.findById(guestId).orElse(null);

        if (owner == null) {
            System.out.println("owner를 찾을 수 없음");
            return false;
        }

        if (guest == null) {
            System.out.println("guest를 찾을 수 없음");
            return false;
        }

        System.out.println("방명록 저장 시작");

        GuestbookEntity guestbook = GuestbookEntity.builder()
                .owner(owner)
                .guest(guest)
                .title(guestbookPostDto.getTitle())
                .content(guestbookPostDto.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        guestbookRepository.save(guestbook);
        System.out.println("방명록 저장 완료");
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
                GuestbookCommentDto commentDto = GuestbookCommentDto.builder()
                        .commentId(comment.getCommentId())
                        .nickname(comment.getUser().getNickname())
                        .content(comment.getContent())
                        .createdAt(comment.getCreatedAt())
                        .build();
                guestbookDto.getComments().add(commentDto);
            }

            guestbookDtos.add(guestbookDto);
        }

        return guestbookDtos;
    }
}