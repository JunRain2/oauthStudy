package com.example.oauthstudy.global.refreshtoken;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@RedisHash(value = "refreshToken")
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class RefreshToken {

    @Id
    private String email;

    private String refreshToken;

    @TimeToLive
    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public RefreshToken(String email) {
        this.email = email;
    }
}