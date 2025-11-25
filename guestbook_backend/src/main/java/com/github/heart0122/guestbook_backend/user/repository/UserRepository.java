package com.github.heart0122.guestbook_backend.user.repository;

import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByLoginId(String loginId);
    UserEntity findByUserId(Long userId);
    UserEntity findByNickname(String username);
    UserEntity save(UserEntity userEntity);
}
