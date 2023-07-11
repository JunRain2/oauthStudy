package com.example.oauthstudy.global.oauth2;

import com.example.oauthstudy.user.domain.entity.Role;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

// 기본으로 반환되는 OAuth2User 객체에서 추가할 필드가 있기 때문에 사용
@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    // 추가한 필드
    /**
     * email : OAuth를 이용한 첫 로그인 시, Resource Server가 제공하지 않는 정보가 필요할 경우,
     * 내 서비스 내에서 해당 정보를 사용자에게 입력 받아야함
     * 하지만 어떤 유저가 OAuth 로그인한지 내 서비스의 서버 입장에서는 알 수 없으므로 OAuth 로그인 시 임의의 Eamil을 생성하여
     * AccessToken을 발급받아서 회원 식별용으로 AccessToken을 사용
     *
     * role : OAuth 로그인 시 추가 정보를 판단하기 위해 필요
     * 처음 로그인하는 유저를 ROLE_GUEST로 설정하고, 이후 추거 장보를 입력해서 회원가입 진행시, ROLE_USER로 압데이트
     * OAuth 로그인 회원 중 Role.GUST인 회원은 처음 로그인이므로 SuccessHandler에서 추가 정보를 입력하는 URL로 리다이렉트
     */
    private String email;
    private Role role;

    /**
     * Constructs a {@code DefaultOAuth2User} using the provided parameters.
     *
     * @param authorities      the authorities granted to the user
     * @param attributes       the attributes about the user
     * @param nameAttributeKey the key used to access the user's &quot;name&quot; from
     *                         {@link #getAttributes()}
     */
    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes,
                            String nameAttributeKey, String email, Role role) {
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.role = role;
    }
}
