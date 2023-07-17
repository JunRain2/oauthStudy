package com.example.oauthstudy.user.controller;

import com.example.oauthstudy.global.jwt.service.JwtService;
import com.example.oauthstudy.user.dto.Oauth2UserDto;
import com.example.oauthstudy.user.dto.UserSignUpDto;
import com.example.oauthstudy.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.oauthstudy.global.HttpStatusResponseEntity.RESPONSE_BAD_REQUEST;
import static com.example.oauthstudy.global.HttpStatusResponseEntity.RESPONSE_OK;

@RestController
@RequiredArgsConstructor
public class UserController {

    final private UserService userService;
    final private JwtService jwtService;

    @PostMapping("/sign-up")
    public ResponseEntity<HttpStatus> signUp(@RequestBody UserSignUpDto userSignUpDto){
        if(userService.signUp(userSignUpDto)) {
            return RESPONSE_OK;
        }
        return RESPONSE_BAD_REQUEST;
    }

    @PostMapping("/oauth2/sign-up")
    private ResponseEntity<HttpStatus> signUp(@RequestBody Oauth2UserDto oauth2UserDto, HttpServletRequest request) {
        String accessToken = jwtService.extractAccessToken(request).orElseThrow();
        String email = jwtService.extractEmail(accessToken).orElseThrow();

        userService.signUp(email, oauth2UserDto);

        return RESPONSE_OK;

    }

    @GetMapping("/logout")
    private ResponseEntity<HttpStatus> logout(HttpServletRequest request) {

        return RESPONSE_OK;
    }


    @GetMapping("/jwt-test")
    public ResponseEntity<HttpStatus> jwtTest() {
        return RESPONSE_OK;
    }
}
