package com.example.oauthstudy.user.exception;

public class MemberNotFoundException extends RuntimeException{
    public MemberNotFoundException() {
    }

    public MemberNotFoundException(String message) {
        super(message);
    }

    public MemberNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
