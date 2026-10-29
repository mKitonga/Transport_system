package com.transport.authentication.Validator;

import com.transport.user.entity.User;
import com.transport.user.repository.TransportUserRepository;
import com.transport.liby.service.BaseJpaRepoReadService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniquePhoneNumberValidator extends BaseJpaRepoReadService<User, TransportUserRepository> implements ConstraintValidator<UniquePhoneNumber, String> {

    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return true;
        }

        var spec = repository.notDeleted()
                .and(repository.phoneNumberIs(phoneNumber));

        return !repository.exists(spec);
    }
}
