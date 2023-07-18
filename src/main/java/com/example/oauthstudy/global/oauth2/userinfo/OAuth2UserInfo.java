package com.example.oauthstudy.global.oauth2.userinfo;

import java.util.Map;

// 소셜 타입별로 유저 정보를 가지는 추상 클래스
// OAuth2UserInfo 추상 클래스를 상속받아 각 소셜 타입의 유저 정보 클래스를 구현
public abstract class OAuth2UserInfo {

    // 상속 받는 클래스만 사용하기 위해 protected 사용
    protected Map<String, Object> attributes;

    // 각 소셜 타입별로 유저 정보 attribute를 주입받아, 각 소셜 타입별 유저 정보 클래스가 소셜 타입에 맞는 attirbute를 주입받아 가지도록 함
    public OAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public abstract String getId();

    public abstract String getNickname();

    public abstract String getImageUrl();

    public abstract String getEmail();
}
