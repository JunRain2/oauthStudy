package com.example.oauthstudy.global.blacklist;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RedisHash(value = "blackList")
public class BlackList {

    @Id
    private String accessToken;

    @TimeToLive
    private Long expiration;

    public BlackList(String accessToken) {
        this.accessToken = accessToken;
    }

    public void setExpiration(Long expiration) {
        this.expiration = expiration;
    }
}
