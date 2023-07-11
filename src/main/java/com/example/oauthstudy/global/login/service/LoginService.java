package com.example.oauthstudy.global.login.service;

import com.example.oauthstudy.user.domain.entity.User;
import com.example.oauthstudy.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * UsernamePasswordAuthenticationToken 을 AuthenticationManager 즉 Providermanager에 전달
 * ProvideManager는 전달받은 UsernamePasswordAuthenticationToken를 ProviderManager의 구현체인 DaoAuthenticationProvider에게 전달
 * DaoAuthenticationProvider는 UserDetailsService의 loadUserByUsername(String username)을 호출하여 UserDetails객체를 반환
 * username은 UsernamePasswordAuthenticationToken 에서 꺼내어 설정
 * loadUserByUsername를 통해 DB의 유저를 찾아 있다면, UserEntity를 반환하고, 그 Entity로 UserDetails 객체로 만들어 반환
 * UserDetails 객체의 password와 PasswordEncoder의 password가 일치하는지 확인
 * 일치하다면, UsernamePasswordAuthenticationToken에 UserDetails 객체와 Authorities를 담아서 반환.
 * ProviderManager에서 UsernamePasswordAuthenticationToken으로 인증 객체를 생성하여 인증 성공 처리
 */
@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        //
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일이 존재하지 않습니다."));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
