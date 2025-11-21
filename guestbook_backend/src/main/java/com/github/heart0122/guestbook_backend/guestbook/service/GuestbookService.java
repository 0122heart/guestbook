package com.github.heart0122.guestbook_backend.guestbook.service;

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

    public boolean patch(GuestbookPatchDto guestbookPatchDto){
        GuestbookEntity guestbook = guestbookRepository.findById(guestbookPatchDto.getGuestbookId())
                .orElse(null);
        if(guestbook == null) {
            return false;
        }

        guestbook.setTitle(guestbookPatchDto.getTitle());
        guestbook.setContent(guestbookPatchDto.getContent());
        guestbookRepository.save(guestbook);

        return true;
    }

    public boolean delete(GuestbookPatchDto guestbookPatchDto){
        try{
            guestbookRepository.deleteByGuestbookId(guestbookPatchDto.getGuestbookId());
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public List<GuestbookPostDto> read(String userNickname) {
        UserEntity userEntity = userRepository.findByNickname(userNickname);
        List<GuestbookPostDto> guestbookPostDtos = new ArrayList<>();
        for(var ue : guestbookRepository.readByOwnerId(userEntity.getUserId())){
            GuestbookPostDto guestbookPostDto = new GuestbookPostDto();
            guestbookPostDto.setId(ue.getGuestbookId());
            guestbookPostDto.setOwnerNickname(userEntity.getNickname());
            guestbookPostDto.setGuestNickname(ue.getGuest().getNickname());
            guestbookPostDto.setTitle(ue.getTitle());
            guestbookPostDto.setContent(ue.getContent());
            guestbookPostDto.setCreatedAt(ue.getCreatedAt());
            guestbookPostDtos.add(guestbookPostDto);
        }
        return guestbookPostDtos;
    }
}
