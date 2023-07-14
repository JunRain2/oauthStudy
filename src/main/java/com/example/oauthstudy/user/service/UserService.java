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
                || userRepository.existsByNickname(userSignUpDto.getNickName())) {
            return false;
        }

        User user = UserSignUpDto.toEntity(userSignUpDto);

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);

        return true;
    }


}
