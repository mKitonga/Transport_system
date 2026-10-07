package com.transport.Authentication.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public abstract class IntegrationAuthenticationService {
    private BCryptPasswordEncoder passwordEncoder;
    protected String hashedApiKey;

    public boolean isValid(String rawApiKey) {
        return passwordEncoder.matches(rawApiKey, hashedApiKey);
    }

    @Autowired
    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
