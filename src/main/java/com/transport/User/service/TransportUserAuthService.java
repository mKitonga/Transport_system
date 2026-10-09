package com.transport.User.service;

import com.transport.User.entity.User;
import com.transport.User.entity.UserType;
import com.transport.User.repository.TransportUserRepository;
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
