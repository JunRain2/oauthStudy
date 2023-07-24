package com.example.oauthstudy.global.resolver;


import com.example.oauthstudy.global.anotation.LoginUser;
import com.example.oauthstudy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

// @LoginMember 적용된 매개 변수를 처리
@RequiredArgsConstructor
@Component
@Slf4j
// HandlerMethodArgumentResolver는 컨트롤러 메서드의 매개변수를 해석하고 해당 매개변수에 값을 제공
public class LoginUserArgumentResolver implements HandlerMethodArgumentResolver {

    private final UserService userService;

    @Override
    // 해당 Resolver가 주어진 매개변수를 처리할 수 있는지 여부를 결정
    public boolean supportsParameter(MethodParameter methodParameter) {
        // @LoginMember가 있는지 확인
        log.info("@LoginMember가 있는지 확인");
        return methodParameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    // 매개 변수에 값을 제공하는 로직
    // 인자 1. 현재 처리중인 매개변수의 정보 2. 주로 뷰의 이름이나 모델 설정에 사용하는 객체 3. 웹 요청과 관련된 정보를 얻을 수 있다. 4. 데이터 바인딩 및 유효성 거사에 상숑
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer,
                                  NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        // 로그인한 회원의 정보를 가져와 매개변수에 반환
        log.info("어노테이션 실행");
        return userService.getLoginUser();
    }
}
