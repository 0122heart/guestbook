package com.github.heart0122.guestbook_backend.user;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Component
@SessionScope
public class KeepLoginComponent implements Serializable {
    @Serial
    private final static long serialVersionUID = 1L;

    private Long id = null;
    private String nickname = null;

    public boolean isLogin(){
        if(id == null || nickname == null) return false;
        return true;
    }

    public Map<String, Object> getCurrentUser() {
        if (id == null || nickname == null) return null;

        Map<String, Object> response = new HashMap<>();
        response.put("id", id);
        response.put("nickname", nickname);

        return response;
    }
}
