package com.github.heart0122.guestbook_backend.guestbook.service;

import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookCommentDto;
import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookListDto;
import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookPatchDto;
import com.github.heart0122.guestbook_backend.guestbook.dto.GuestbookPostDto;
import com.github.heart0122.guestbook_backend.guestbook.entity.GuestbookEntity;
import com.github.heart0122.guestbook_backend.guestbook.repository.GuestbookRepository;
import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import com.github.heart0122.guestbook_backend.user.repository.UserRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Data
@Service
public class GuestbookService {
    private final GuestbookRepository guestbookRepository;
    private final UserRepository userRepository;

    public boolean post(GuestbookPostDto guestbookPostDto){
        UserEntity owner = userRepository.findByNickname(guestbookPostDto.getOwnerNickname());
        UserEntity guest = userRepository.findByNickname(guestbookPostDto.getGuestNickname());

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
            guestbookRepository.deleteByGuestbookId(guestbookId);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public List<GuestbookListDto> read(String userNickname) {
        UserEntity userEntity = userRepository.findByNickname(userNickname);
        List<GuestbookListDto> guestbookListDtos = new ArrayList<>();
        for(var ue : guestbookRepository.findGuestbookEntitiesByOwner(
                userRepository.findByUserId(userEntity.getUserId()))){
            GuestbookListDto guestbookListDto = new GuestbookListDto();
            guestbookListDto.setId(ue.getGuestbookId());
            guestbookListDto.setOwnerNickname(userEntity.getNickname());
            guestbookListDto.setGuestNickname(ue.getGuest().getNickname());
            guestbookListDto.setTitle(ue.getTitle());
            guestbookListDto.setContent(ue.getContent());
            guestbookListDto.setCreatedAt(ue.getCreatedAt());
            guestbookListDto.setComments(new ArrayList<>());
            for(var c : ue.getComments()){
                GuestbookCommentDto guestbookCommentDto = new GuestbookCommentDto();
                guestbookCommentDto.setCommentId(c.getCommentId());
                guestbookCommentDto.setNickname(c.getUser().getNickname());
                guestbookCommentDto.setContent(c.getContent());
                guestbookCommentDto.setCreatedAt(c.getCreatedAt());
                guestbookListDto.getComments().add(guestbookCommentDto);
                guestbookListDtos.add(guestbookListDto);
            }
        }
        return guestbookListDtos;
    }
}
