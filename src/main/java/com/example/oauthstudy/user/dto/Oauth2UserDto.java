package com.example.oauthstudy.user.dto;

import com.example.oauthstudy.user.domain.entity.Role;
import com.example.oauthstudy.user.domain.entity.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class Oauth2UserDto {

    private int age;

    private String city;

}
