package com.example.oauthstudy.global.advice;

import com.example.oauthstudy.user.exception.DuplicationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import static com.example.oauthstudy.global.HttpStatusResponseEntity.RESPONSE_BAD_REQUEST;

@RestController
public class ExceptionAdvice {

    @ExceptionHandler(DuplicationException.class)
    ResponseEntity<HttpStatus> duplicationException() {
        return RESPONSE_BAD_REQUEST;
    }
}
