package com.transport.user.service;

import com.transport.parents.entity.Parent;
import com.transport.parents.repository.ParentRepository;
import com.transport.user.entity.UserType;
import org.springframework.stereotype.Service;

@Service
public class ParentAuthService extends UserAuthService<Parent, ParentRepository> {
    @Override
    public UserType getUserType() {
        return UserType.PARENT;
    }

    @Override
    protected Parent getNewUser() {
        return new Parent();
    }
}
