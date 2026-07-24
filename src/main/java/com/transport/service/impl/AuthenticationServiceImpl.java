package com.transport.service.impl;

import java.util.Optional;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.transport.dto.request.LoginRequest;
import com.transport.dto.request.RegisterRequest;
import com.transport.dto.response.AuthenticationResponse;
import com.transport.entity.User;
import com.transport.repository.UserRepository;
import com.transport.security.JwtService;
import com.transport.service.AuthenticationService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthenticationResponse register(RegisterRequest request) {

        // Check if passwords match
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match.");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already registered.");
        }

        // Check if phone number already exists
        if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
            throw new RuntimeException("Phone number is already registered.");
        }

        // Split full name into first and last name
        String[] names = request.getFullName().trim().split("\\s+", 2);

        String firstName = names[0];
        String lastName = names.length > 1 ? names[1] : "";

        // Create user
        User user = new User();

        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());
        user.setActive(true);

        // Save user
        userRepository.save(user);

        // Generate JWT
        String token = jwtService.generateToken(user.getEmail());

        return new AuthenticationResponse(
                token,
                "User registered successfully.",
                user.getRole().name()
        );
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        // Authenticate user credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );

        // Find the user
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());

        if (optionalUser.isEmpty()) {
            throw new RuntimeException("Invalid email or password.");
        }

        User user = optionalUser.get();

        // Generate JWT
        String token = jwtService.generateToken(user.getEmail());

        return new AuthenticationResponse(
                token,
                "Login successful.",
                user.getRole().name()
        );
    }
}