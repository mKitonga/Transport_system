package com.transport.user.service;

import com.transport.user.entity.User;
import com.transport.user.entity.UserType;
import com.transport.user.repository.TransportUserRepository;
import org.springframework.stereotype.Service;

@Service
public class TransportUserAuthService extends UserAuthService<User, TransportUserRepository> {
    @Override
    public UserType getUserType() {
        return UserType.PARENT;
    }

    @Override
    protected User getNewUser() {
        return new User();
    }
}
