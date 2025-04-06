package com.example.authservice.service;


import com.example.authservice.dto.AuthRequest;
import com.example.authservice.dto.RegisterRequest;
import com.example.authservice.model.User;

public interface AuthService {
    String authenticate(AuthRequest request);
    User register(RegisterRequest request);

    boolean getUserDetails(Integer id, String emailId);
}
