package com.example.oauthstudy.user.service;

import com.example.oauthstudy.user.domain.entity.Role;
import com.example.oauthstudy.user.domain.entity.User;
import com.example.oauthstudy.user.domain.repository.UserRepository;
import com.example.oauthstudy.user.dto.UserSignUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean signUp(UserSignUpDto userSignUpDto){
        if (userRepository.existsByEmail(userSignUpDto.getEmail())
                || userRepository.existsByNickName(userSignUpDto.getNickName())) {
            return false;
        }

        User user = User.builder()
                .email(userSignUpDto.getEmail())
                .password(userSignUpDto.getPassword())
                .nickName(userSignUpDto.getNickName())
                .age(userSignUpDto.getAge())
                .city(userSignUpDto.getCity())
                .role(Role.USER)
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);

        return true;
    }


}
