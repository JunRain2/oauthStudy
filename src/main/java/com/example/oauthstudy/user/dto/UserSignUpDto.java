package com.example.oauthstudy.user.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UserSignUpDto {
    private String email;
    private String password;
    private String nickName;
    private int age;
    private String city;
}
