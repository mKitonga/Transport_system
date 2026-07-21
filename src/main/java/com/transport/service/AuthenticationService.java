package com.transport.service;

import com.transport.dto.request.LoginRequest;
import com.transport.dto.request.RegisterRequest;
import com.transport.dto.response.AuthenticationResponse;

/**
 * Service interface for handling user authentication and registration.
 */
public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);
    
}
