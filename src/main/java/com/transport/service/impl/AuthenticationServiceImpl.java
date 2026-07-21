package com.transport.service.impl;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.transport.dto.request.LoginRequest;
import com.transport.dto.request.RegisterRequest;
import com.transport.dto.response.AuthenticationResponse;
import com.transport.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

/**
 * Implementation of the AuthenticationService.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    
    private final PasswordEncoder passwordEncoder;



    @Override
    public AuthenticationResponse register(RegisterRequest request) {

        // Encode password to ensure the injected PasswordEncoder is used.
        passwordEncoder.encode(request.getPassword());

        // Registration logic will be added here

        return new AuthenticationResponse(
                null,
                "User registered successfully.",
                request.getRole().name()
        );
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        // Login logic will be added here

        return new AuthenticationResponse(
                null,
                "Login successful.",
                null
        );
    }
}