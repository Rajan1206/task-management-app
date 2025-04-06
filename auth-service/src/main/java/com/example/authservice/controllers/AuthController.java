package com.example.authservice.controllers;

import com.example.authservice.dto.AuthRequest;
import com.example.authservice.dto.AuthResponse;
import com.example.authservice.dto.LoginResponse;
import com.example.authservice.dto.RegisterRequest;
import com.example.authservice.model.User;
import com.example.authservice.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody AuthRequest request) throws RuntimeException {

        try {
            String token = authService.authenticate(request);
            return ResponseEntity.ok(AuthResponse.builder().token(token).message("Login successfully").build());

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<Object> register(@RequestBody RegisterRequest request) {
        try {
            User userRegister = authService.register(request);
            LoginResponse loginResponse = LoginResponse.builder()
                                                       .id(userRegister.getId())
                                                       .email(userRegister.getEmail())
                                                       .fullName(userRegister.getFullName())
                                                       .userName(userRegister.getUsername())
                                                       .message("User registered successfully")
                                                       .build();
            return ResponseEntity.ok(loginResponse);
        } catch (Exception e) {
            log.error("Error while registering user", e);
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("X-User-ID") Integer userId,
                                         @RequestHeader("X-User-Email") String emailId) {
        boolean isPresent = authService.getUserDetails(userId, emailId);
        if (isPresent) {
            // we could store this token in Redis or DB to blacklist user access with same token
            log.info("Token marked as logged out: " + emailId);
        }
        return ResponseEntity.ok("User logged out successfully");
    }
}
