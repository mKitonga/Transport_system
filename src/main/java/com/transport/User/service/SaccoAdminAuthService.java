package com.transport.User.service;

import com.transport.Sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.Sacco.SaccoAdmin.Repository.SaccoAdminRepository;
import com.transport.User.entity.UserType;
import org.springframework.stereotype.Service;

@Service
public class SaccoAdminAuthService extends UserAuthService<SaccoAdmin, SaccoAdminRepository> {
    @Override
    public UserType getUserType() {
        return UserType.SACCO_ADMIN;
    }

    @Override
    protected SaccoAdmin getNewUser() {
        return new SaccoAdmin();
    }
}
