package com.example.oauthstudy.global.jwt.filter;

import com.example.oauthstudy.global.exception.InvalidAccessTokenException;
import com.example.oauthstudy.global.jwt.service.JwtService;
import com.example.oauthstudy.global.jwt.util.PasswordUtil;
import com.example.oauthstudy.global.refreshtoken.RefreshToken;
import com.example.oauthstudy.global.refreshtoken.RefreshTokenRepository;
import com.example.oauthstudy.user.domain.entity.User;
import com.example.oauthstudy.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * Jwt 인증 필터
 * "/login" 이외의 URI 요청이 왔을때 처리하는 필터
 * <p>
 * 기본적으로 사용자는 요청 헤더에 AccessToken만을 담아서 요청
 * AccessToken 만료 시에만 RefreshToken을 요청 헤더에 AccessToken과 함께 요청
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/login";

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // "/login"에 대해 인증절차는 수행하지만, 해당 필터는 수행하지 않음
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); // "/login" 요청이 들어오면, 다음 필터 호출
            return; // return 으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
        }

        // 요청 헤더에서 RefreshToken 추출 -> RefreshToken이 없거나 유효하지 않다면(DB에 저장된 RefreshToken과 다르다면) null을 반환
        // 사용자의 요청 헤더에 RefreshToken이 있는 경우는, AccessToken이 만료되어 요청하는 경우 -> 이외의 상황은 모두 null
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        // Refresh Token이 존재하다면, DB의 Refresh Token과 일치하는지 판단 후, 일치하면 AccessToken을 재발급
        if (refreshToken != null) {
            String email = jwtService.extractEmail(refreshToken).orElseThrow(InvalidAccessTokenException::new);
            checkRefreshTokenAndReIssueAccessToken(response, email);
            return; // RefreshToken을 보낸 경우 AccessToken을 재발급 하고 인증 처리는 하지 않게 하기위해 바로 return으로 필터 진행 막기
        }

        // RefreshToken으 없거나 유효하지 않다면, RefreshToken을 검사하고 인증을 처리하는 로직 수행
        // RefreshToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
        // RefreshToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    private void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                   FilterChain filterChain) throws ServletException, IOException {
        log.info("checkAccessTokenAndAuthentication() 호출");
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .ifPresent(accessToken -> jwtService.extractEmail(accessToken) // 이메일 추출
                        .ifPresent(email -> userRepository.findByEmail(email) // 이메일로 user 찾기
                                .ifPresent(this::saveAuthentication))); // 해당 유저를 인증 처리

        filterChain.doFilter(request, response);
    }

    // 인증 허가 메서드
    private void saveAuthentication(User myUser) {
        String password = myUser.getPassword();
        if (password == null) { // 소셜 로그인 유저의 비밀번호를 임의로 설정하여 소셜 로그인 유저도 인증되도록 설정
            password = PasswordUtil.generateRandomPassword();
        }

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getEmail())
                .password(password)
                .roles(myUser.getRole().name()) // authorities에 저장
                .build();

        // UsernamePasswordAuthenticationToken() 인증 객체인 Authentication 객체 생성
        // 피라미터 : userDetailsUser 객체, credential(보통 비밀번호, 인증 시에는 보통 null로 제거), Collection < ? extends GrantedAuthority>
        // UserDetails의 User객체 안에 Set<GrandAuthorty> authorities이 있어서 getter로 호출한 후,
        // new NullAuthoritiesMapper()로 GrantedAuthoritiesMapper 객체를 생성하고 mapAuthorities()에 담기

        // GrantedAuthoritiesMapper를 구현한 객체가 NullAuthoritiesMapper
        // GrantedAuthoritiesMapper는 사용자의 인증된 권한(GrantedAuthority) 목록을 변환하는 데 사용

        // SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후,
        // setAuthentication()을 이용하여 위에서 만든 Authentication 객체에 대한 인증 허가 처리
        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    // Refresh Token을 통해 DB에서 유저를 찾고, Access Token을 재발급
    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String email) {
        RefreshToken token = refreshTokenRepository.findById(email).orElseThrow();
        String reIssuedRefreshToken = reIssueRefreshToken(token, email);
        jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(token.getEmail()
        ), reIssuedRefreshToken);
    }

    // Refresh Token 재발급 & Redis에 Refresh Token 업데이트
    private String reIssueRefreshToken(RefreshToken refreshToken, String email) {
        String reIssuedRefreshToken = jwtService.createRefreshToken(email);
        refreshToken.setRefreshTokenAndTimeToLive(reIssuedRefreshToken,
                jwtService.getRefreshTokenExpirationPeriod()/1000);
        refreshTokenRepository.save(refreshToken);
        return reIssuedRefreshToken;
    }
}
