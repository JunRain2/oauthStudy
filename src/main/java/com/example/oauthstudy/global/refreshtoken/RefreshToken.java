package com.example.oauthstudy.global.refreshtoken;

import lombok.*;
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

    @TimeToLive()
    private Long refreshTokenExpirationPeriod;

    public void setRefreshTokenAndTimeToLive(String refreshToken, Long Expiration) {
        this.refreshToken = refreshToken;
        this.refreshTokenExpirationPeriod = Expiration;

    }

    public RefreshToken(String email) {
        this.email = email;
    }
}