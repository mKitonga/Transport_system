package com.transport.user.service;

import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.school.SchoolAdmin.Repository.SchoolAdminRepository;
import com.transport.user.entity.UserType;
import org.springframework.stereotype.Service;

@Service
public class SchoolAdminAuthService extends UserAuthService<SchoolAdmin, SchoolAdminRepository> {
    @Override
    public UserType getUserType() {
        return UserType.SCHOOL_ADMIN;
    }

    @Override
    protected SchoolAdmin getNewUser() {
        return new SchoolAdmin();
    }
}
