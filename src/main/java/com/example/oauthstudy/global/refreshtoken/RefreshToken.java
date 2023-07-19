package com.example.oauthstudy.global.refreshtoken;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;


@RedisHash(value = "refreshToken", timeToLive = 3600)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RefreshToken {

    @Id
    private String email;

    private String refreshToken;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RefreshToken(String email) {
        this.email = email;
    }
}