package com.example.oauthstudy.global.login.handler;

import com.example.oauthstudy.global.jwt.service.JwtService;
import com.example.oauthstudy.global.refreshtoken.RefreshToken;
import com.example.oauthstudy.global.refreshtoken.RefreshTokenRepository;
import com.example.oauthstudy.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

// 로그인 인증 성공시 핸들러
@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    // JSON 로그인 필터를 정상적으로 통과해서 인증처리가 됐기 때문에, AccessToken과 RefreshToken을 발급
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) {
        String email = extractUsername(authentication); // 인증 정보에서 Username인 email 추출
        String accessToken = jwtService.createAccessToken(email);
        String refreshToken = jwtService.createRefreshToken(email);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        // 회원가입시 RefreshToken이 null이기 때문에 로그인 성공 시 redis에 따로 저장
        RefreshToken token = refreshTokenRepository.findById(email)
                .orElse(RefreshToken.builder()
                        .email(email)
                        .refreshTokenExpirationPeriod(jwtService.getRefreshTokenExpirationPeriod())
                        .build());
        token.updateRefreshToken(refreshToken);
        refreshTokenRepository.save(token);

        log.info("로그인에 성공하였습니다. 이메일 : {}", email);
        log.info("로그인에 성공하였습니다. AccessToken : {}", accessToken);
        log.info("발급된 AccessToken 만료 기간 : {}", accessTokenExpiration);
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal(); // UserDetails를 반환
        return userDetails.getUsername(); // username -> email
    }

}
