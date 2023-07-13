package com.example.oauthstudy.user.domain.repository;

import com.example.oauthstudy.user.domain.entity.SocialType;
import com.example.oauthstudy.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String email);

    boolean existsByNickName(String nickName);

    Optional<User> findByEmail(String email);

    Optional<User> findByNickName(String nickName);

    Optional<User> findByRefreshToken(String refreshToken);

    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);
}
