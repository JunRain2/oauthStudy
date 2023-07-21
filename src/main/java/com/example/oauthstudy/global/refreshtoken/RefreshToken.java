package com.example.oauthstudy.global.refreshtoken;

import lombok.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;


@RedisHash(value = "refreshToken", timeToLive = 1209600)
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

    @Builder
    public RefreshToken(String email, String refreshToken, Long refreshTokenExpirationPeriod) {
        this.email = email;
        this.refreshToken = refreshToken;
        this.refreshTokenExpirationPeriod = refreshTokenExpirationPeriod/1000;
    }
}