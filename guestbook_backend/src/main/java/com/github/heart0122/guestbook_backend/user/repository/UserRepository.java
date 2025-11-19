package com.github.heart0122.guestbook_backend.user.repository;

import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public UserEntity findByLoginId(String loginId);
    public UserEntity findByEmail(String email);
    public UserEntity findByNickname(String username);
    public UserEntity save(UserEntity userEntity);
}
