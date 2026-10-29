package com.transport.user.service;

import com.transport.school.SchoolDriver.Entity.SchoolDriver;
import com.transport.school.SchoolDriver.Repository.SchoolDriverRepository;
import com.transport.user.entity.UserType;
import org.springframework.stereotype.Service;

@Service
public class SchoolDriverAuthService extends UserAuthService<SchoolDriver, SchoolDriverRepository> {
    @Override
    public UserType getUserType() {
        return UserType.SCHOOL_DRIVER;
    }

    @Override
    protected SchoolDriver getNewUser() {
        return new SchoolDriver();
    }
}
