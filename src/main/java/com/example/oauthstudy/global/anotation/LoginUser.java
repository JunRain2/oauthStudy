package com.example.oauthstudy.global.anotation;
import java.lang.annotation.*;

@Target({ElementType.PARAMETER}) // 어노테이션을 매개변수에 적용될 수 있음을 나타냄
@Retention(RetentionPolicy.RUNTIME) // 어노테이션 정보가 런타임동안 유지됨을 나타냄, 즉 어노테이션 정보를 프로그램이 동적으로 활용함을 나타냄
@Documented // 어노테이션에대한 문서를 자동으로 생성
public @interface LoginUser { // @LoginMember 어노테이션은 사용자의 인증 상태를 나타내는 객체를 주입받기 위해 사용
}

