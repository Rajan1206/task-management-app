package com.example.authservice.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class SecurityExceptions {

    private SecurityExceptions() {
        super();
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class AuthException extends RuntimeException {

        public AuthException(String message) {
            super(message);
        }
    }
}
