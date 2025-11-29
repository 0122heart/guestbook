package com.github.heart0122.guestbook_backend.user.repository;

import com.github.heart0122.guestbook_backend.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByLoginId(String loginId);
    Optional<UserEntity> findByNickname(String username);
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
}
