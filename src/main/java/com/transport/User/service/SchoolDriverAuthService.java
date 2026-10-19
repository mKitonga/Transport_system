package com.transport.User.service;

import com.transport.School.SchoolDriver.Entity.SchoolDriver;
import com.transport.School.SchoolDriver.Repository.SchoolDriverRepository;
import com.transport.User.entity.UserType;
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
