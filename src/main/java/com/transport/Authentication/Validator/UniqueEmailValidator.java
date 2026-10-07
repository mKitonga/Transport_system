package com.transport.Authentication.Validator;

import com.transport.User.entity.User;
import com.transport.User.repository.TransportUserRepository;
import com.transport.User.repository.UserRepository;
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
