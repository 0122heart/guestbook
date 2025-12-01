package com.github.heart0122.guestbook_backend.user.service;

import com.github.heart0122.guestbook_backend.user.dto.UserDto;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
@Component
@SessionScope
public class KeepLoginService implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long id = null;
    private String nickname = null;

    public boolean isLogin(){
        if(id == null || nickname == null) return false;
        return true;
    }

    public UserDto getCurrentUser() {
        if (id == null || nickname == null) return null;

        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setNickname(nickname);

        return userDto;
    }
}
