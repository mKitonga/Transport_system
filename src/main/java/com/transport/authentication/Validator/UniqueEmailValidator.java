package com.transport.authentication.Validator;

import com.transport.user.entity.User;
import com.transport.user.repository.TransportUserRepository;
import com.transport.liby.service.BaseJpaRepoReadService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueEmailValidator extends BaseJpaRepoReadService<User, TransportUserRepository> implements ConstraintValidator<UniqueEmail, String> {

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.isBlank()) {
            return true;
        }

        var spec = repository.notDeleted()
                .and(repository.emailIs(email));

        return !repository.exists(spec);
    }
}
