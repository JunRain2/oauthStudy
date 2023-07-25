package com.example.oauthstudy.global.logout.handler;

import com.example.oauthstudy.global.jwt.service.JwtService;
import com.example.oauthstudy.global.refreshtoken.RefreshToken;
import com.example.oauthstudy.global.refreshtoken.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
@Slf4j
public class SimpleUrlLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
        implements LogoutSuccessHandler {

    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;

    // 로그아웃 성공시 이메일에 해당하는 redis에서 refreshToken 삭제
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) {
        String accessToken = jwtService.extractAccessToken(request).orElseThrow();
        String email = jwtService.extractEmail(accessToken).orElseThrow();

        logout(email, accessToken);
    }

    public void logout(String email, String accessToken) {
        RefreshToken refreshToken = refreshTokenRepository.findById(email).orElseThrow();
        refreshTokenRepository.delete(refreshToken);
        log.info("로그아웃 성공");

    }
}
