package com.example.oauthstudy.global.login.service;

import com.example.oauthstudy.user.domain.entity.User;
import com.example.oauthstudy.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UsernamePasswordAuthenticationToken 흐름 :
 * 필터에서 AuthenticationManager 의 구현체인 ProviderManager 에 전달
 * ProviderManager의 구현체인 DaoAuthenticationProvider 에 전달
 * DaoAuthenticationProvider는 UserDetailService의 loadUserByUsername 을 호출하여 UserDetails를 반환
 * (DB에 해당 유저가 있는지 확인 후 반환)
 * 존재한다면 PasswrodEncorder 를 이용해 passowrd가 일치하는지 확인
 * 일치하다면 UsernamePasswordAuthenticationToken에 UserDetails 객체와 Authorities 를 담아서 반환하여 인증 성공을 처리
 */
@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    // UserDetails의 User 객체를 만들어서 반환하는 역할 -> email을 이용해서 반환
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name()) // 'ROEL_' 가 접두사로 붙었는지 검사
                .build();
    }
}
