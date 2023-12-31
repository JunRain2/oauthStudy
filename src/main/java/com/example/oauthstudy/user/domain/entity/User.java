package com.example.oauthstudy.user.domain.entity;

import com.example.oauthstudy.global.BaseTimeEntity;
import com.example.oauthstudy.user.dto.Oauth2UserDto;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;


@Entity
@Getter
@Table(name = "USERS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id
    @Column(name="USER_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String nickname;
    private String imageUrl;
    private Integer age;
    private String city;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    private String socialId; // 로그인한 소셜 타입의 식별자 값 (일반 로그인시 null)

    public void authorizeUser() {
        this.role = Role.USER;
    }

    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }


    @Builder
    public User(String email, String password, String nickname,
                String imageUrl, Integer age, String city, Role role, SocialType socialType, String socialId) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.age = age;
        this.city = city;
        this.role = role;
        this.socialType = socialType;
        this.socialId = socialId;
    }

    public void signUp(Oauth2UserDto oauth2UserDto) {
        this.city = oauth2UserDto.getCity();
        this.age = oauth2UserDto.getAge();
        this.role = Role.USER;
    }
}
