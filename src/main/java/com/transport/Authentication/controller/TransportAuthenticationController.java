package com.transport.Authentication.controller;

import com.transport.User.entity.User;
import com.transport.User.service.TransportUserAuthService;
import com.transport.User.service.UserAuthService;
import com.transport.liby.service.SystemConfig;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(SystemConfig.STS_USER_BASE_URL + "/auth")
public class TransportAuthenticationController extends AuthenticationController<User> {
    private final TransportUserAuthService authService;

    public TransportAuthenticationController(TransportUserAuthService authService) {
        this.authService = authService;
    }

    @Override
    protected UserAuthService<User, ?> getAuthService() {
        return authService;
    }
}
