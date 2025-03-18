package com.example.authenticationservice.service;

import com.example.authenticationservice.dto.request.LoginRequest;
import com.example.authenticationservice.dto.request.RegisterRequest;
import com.example.authenticationservice.dto.response.AuthResponse;

public interface AuthService {
	AuthResponse register(RegisterRequest request);
	AuthResponse login(LoginRequest request);
	boolean validateToken(String token);
}
