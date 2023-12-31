package com.example.oauthstudy.user.service;

import com.example.oauthstudy.global.jwt.service.JwtService;
import com.example.oauthstudy.user.domain.entity.User;
import com.example.oauthstudy.user.domain.repository.UserRepository;
import com.example.oauthstudy.user.dto.Oauth2UserDto;
import com.example.oauthstudy.user.dto.UserSignUpDto;
import com.example.oauthstudy.user.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final HttpServletRequest request;

    public boolean signUp(UserSignUpDto userSignUpDto) {

        if (userRepository.existsByEmail(userSignUpDto.getEmail())
                || userRepository.existsByNickname(userSignUpDto.getNickName())) {
            return false;
        }

        User user = UserSignUpDto.toEntity(userSignUpDto);

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);

        return true;
    }

    public void signUp(String email, Oauth2UserDto oauth2UserDto) {

        User user = userRepository.findByEmail(email).orElseThrow(MemberNotFoundException::new);

        user.signUp(oauth2UserDto);

    }


    public User getLoginUser() {
        log.info("어노테이션 실행");
        String accessToken = jwtService.extractAccessToken(request).orElseThrow();
        String email = jwtService.extractEmail(accessToken).orElseThrow();

        return userRepository.findByEmail(email).orElseThrow();
    }
}
