package com.example.oauthstudy.global.login.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

// form 데이터를 처리하게 Default인 Spring Security를 JSON 로그인 방식으로 커스텀
public class CustomJsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login"; // /login으로 오는 요청을 처리
    private static final String HTTP_METHOD = "POST"; // 로그인 HTTP 메소드는 POST
    private static final String CONTENT_TYPE = "application/json"; // JSON 타입의 데이터로 오는 로그인 요청만 처리
    private static final String USERNAME_KEY = "email"; // 회원 로그인 시 이메일 요청 JSON Key : "email"
    private static final String PASSWORD_KEY = "password"; // 회원 로그인 시 비밀번호 요청 JSON key : "password"
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER
            = new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); // /login + POST로 온 요청에 매칭

    private final ObjectMapper objectMapper;

    // Java와 Json 사이의 변환을 담당
    public CustomJsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER); // 우리가 만든 필터가 "/login" 시 작동하게 설정
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException("Authentciation Content-Type not supported " + request.getContentType());
        }

        // request.getInputStream() : 현재 HTTP 요청의 메시지 바디를 가져오는 메서드
        // copyToString() : 첫번째 매개변수로는 복사할 InputStream 을, 두번째 매개변수로는 문자 인코딩을 지정
        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        // JSON 요청을 String으로 변환한 messageBody를 objectMapper.readValue() 를 통해 Map으로 변환시켜 각각 Email, Password를 저장
        // readValue() : JSON 문자열을 지정한 타입의 객체로 역직렬화
        Map<String, String> userNamePasswordMap = objectMapper.readValue(messageBody, Map.class);

        String email = userNamePasswordMap.get(USERNAME_KEY);
        String password = userNamePasswordMap.get(PASSWORD_KEY);

        // Principal 과 Credentials 전달
        // 인증 처리 객체인 AuthenticationManager가 인증 시 사용할 인증 대상 객체
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);

        return getAuthenticationManager().authenticate(authRequest);
    }
}
