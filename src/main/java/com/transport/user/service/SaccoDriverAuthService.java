package com.transport.user.service;

import com.transport.sacco.SaccoDriver.Entity.SaccoDriver;
import com.transport.sacco.SaccoDriver.Repository.SaccoDriverRepository;
import com.transport.user.entity.UserType;
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
