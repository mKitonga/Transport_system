package com.transport.Authentication.Validator;


import com.transport.Authentication.Entity.VerificationCode;
import com.transport.Authentication.Repository.VerificationCodeRepository;
import com.transport.liby.service.BaseJpaRepoEditService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Service;

@Service
class VerificationCodeExistsValidator extends BaseJpaRepoEditService<VerificationCode, VerificationCodeRepository>
        implements ConstraintValidator<VerificationCodeExists, String> {

    @Override
    public boolean isValid(String verificationCodeId, ConstraintValidatorContext constraintValidatorContext) {
        return existsByEntityId(verificationCodeId);
    }
}
