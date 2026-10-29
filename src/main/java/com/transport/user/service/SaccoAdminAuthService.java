package com.transport.user.service;

import com.transport.sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.sacco.SaccoAdmin.Repository.SaccoAdminRepository;
import com.transport.user.entity.UserType;
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
