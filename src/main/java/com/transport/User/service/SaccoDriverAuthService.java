package com.transport.User.service;

import com.transport.Sacco.SaccoDriver.Entity.SaccoDriver;
import com.transport.Sacco.SaccoDriver.Repository.SaccoDriverRepository;
import com.transport.User.entity.UserType;
import org.springframework.stereotype.Service;

@Service
public class SaccoDriverAuthService extends UserAuthService<SaccoDriver, SaccoDriverRepository> {
    @Override
    public UserType getUserType() {
        return UserType.SACCO_DRIVER;
    }

    @Override
    protected SaccoDriver getNewUser() {
        return new SaccoDriver();
    }
}
