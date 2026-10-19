package com.transport.User.service;

import com.transport.School.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.School.SchoolAdmin.Repository.SchoolAdminRepository;
import com.transport.User.entity.UserType;
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
