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
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@Service
public class GuestbookService {
    private final GuestbookRepository guestbookRepository;
    private final UserRepository userRepository;
    private final KeepLoginService keepLoginService;

    public boolean post(GuestbookPostDto guestbookPostDto){
        if(!keepLoginService.isLogin()) return false;

        Optional<UserEntity> ownerOpt = userRepository.findById(guestbookPostDto.getOwnerId());
        Optional<UserEntity> guestOpt = userRepository.findById(keepLoginService.getId());
        UserEntity owner = ownerOpt.orElse(null);
        UserEntity guest = guestOpt.orElse(null);

        if(owner == null || guest == null) return false;

        GuestbookEntity guestbook = GuestbookEntity.builder().
                owner(owner).
                guest(guest).
                title(guestbookPostDto.getTitle()).
                content(guestbookPostDto.getContent()).
                build();
        guestbookRepository.save(guestbook);
        return true;
    }

    public boolean patch(Long guestbookId, GuestbookPatchDto guestbookPatchDto){
        GuestbookEntity guestbook = guestbookRepository.findById(guestbookId)
                .orElse(null);
        if(guestbook == null) {
            return false;
        }

        guestbook.setTitle(guestbookPatchDto.getTitle());
        guestbook.setContent(guestbookPatchDto.getContent());
        guestbookRepository.save(guestbook);

        return true;
    }

    public boolean delete(Long guestbookId){
        try{
            guestbookRepository.deleteById(guestbookId);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public List<GuestbookDto> read(String userNickname) {
        Optional<UserEntity> userOpt = userRepository.findByNickname(userNickname);
        UserEntity userEntity = userOpt.orElse(null);

        List<GuestbookDto> guestbookDtos = new ArrayList<>();
        for(var ue : guestbookRepository.findByOwner(userEntity)){
            GuestbookDto guestbookDto = new GuestbookDto();
            guestbookDto.setId(ue.getGuestbookId());
            guestbookDto.setOwnerNickname(userEntity.getNickname());
            guestbookDto.setGuestNickname(ue.getGuest().getNickname());
            guestbookDto.setTitle(ue.getTitle());
            guestbookDto.setContent(ue.getContent());
            guestbookDto.setCreatedAt(ue.getCreatedAt());
            guestbookDto.setComments(new ArrayList<>());
            for(var c : ue.getComments()){
                GuestbookCommentDto guestbookCommentDto = new GuestbookCommentDto();
                guestbookCommentDto.setCommentId(c.getCommentId());
                guestbookCommentDto.setNickname(c.getUser().getNickname());
                guestbookCommentDto.setContent(c.getContent());
                guestbookCommentDto.setCreatedAt(c.getCreatedAt());
                guestbookDto.getComments().add(guestbookCommentDto);
                guestbookDtos.add(guestbookDto);
            }
        }
        return guestbookDtos;
    }
}
