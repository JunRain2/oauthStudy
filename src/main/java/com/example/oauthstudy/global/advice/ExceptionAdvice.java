package com.example.oauthstudy.global.advice;

import com.example.oauthstudy.global.exception.InvalidAccessTokenException;
import com.example.oauthstudy.user.exception.DuplicationException;
import com.example.oauthstudy.user.exception.MemberNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import static com.example.oauthstudy.global.HttpStatusResponseEntity.RESPONSE_BAD_REQUEST;
import static com.example.oauthstudy.global.HttpStatusResponseEntity.RESPONSE_FORBIDDEN;

@RestController
public class ExceptionAdvice {

    @ExceptionHandler(DuplicationException.class)
    ResponseEntity<HttpStatus> duplicationException() {
        return RESPONSE_BAD_REQUEST;
    }

    @ExceptionHandler(MemberNotFoundException.class)
    ResponseEntity<HttpStatus> memberNotFoundException(){
        return RESPONSE_BAD_REQUEST;
    }

    @ExceptionHandler(InvalidAccessTokenException.class)
    ResponseEntity<HttpStatus> invalidAccessToken(){
        return RESPONSE_FORBIDDEN;
    }

}
