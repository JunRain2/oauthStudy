package com.example.oauthstudy.user.controller;

import com.example.oauthstudy.user.dto.UserSignUpDto;
import com.example.oauthstudy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.oauthstudy.global.HttpStatusResponseEntity.RESPONSE_BAD_REQUEST;
import static com.example.oauthstudy.global.HttpStatusResponseEntity.RESPONSE_OK;

@RestController
@RequiredArgsConstructor
public class UserController {

    final private UserService userService;

    @PutMapping("/sign-up")
    public ResponseEntity<HttpStatus> signUp(@RequestBody UserSignUpDto userSignUpDto){
        if(userService.signUp(userSignUpDto)) {
            return RESPONSE_OK;
        }
        return RESPONSE_BAD_REQUEST;
    }

    @GetMapping("/jwt-test")
    public ResponseEntity<HttpStatus> jwtTest() {
        return RESPONSE_OK;
    }
}
