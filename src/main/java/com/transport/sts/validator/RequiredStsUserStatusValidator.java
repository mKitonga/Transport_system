package com.transport.sts.validator;

import com.transport.user.entity.UserStatus;
import com.transport.user.service.StsUserReadService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class RequiredStsUserStatusValidator implements ConstraintValidator<RequiredStsUserStatus, String> {

    private List<UserStatus> statuses;

    private StsUserReadService stsUserReadService;

    @Override
    public void initialize(RequiredStsUserStatus constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.statuses = List.of(constraintAnnotation.statuses());
    }

    @Override
    public boolean isValid(String userId, ConstraintValidatorContext ctx) {
        var user = stsUserReadService.findByEntityId(userId);
        return statuses.contains(user.getUserStatus());
    }

    @Autowired
    public void setStsUserReadService(StsUserReadService service) {
        this.stsUserReadService = service;
    }
}
