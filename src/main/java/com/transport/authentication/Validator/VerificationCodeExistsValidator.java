package com.transport.authentication.Validator;


import com.transport.authentication.Entity.VerificationCode;
import com.transport.authentication.Repository.VerificationCodeRepository;
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
