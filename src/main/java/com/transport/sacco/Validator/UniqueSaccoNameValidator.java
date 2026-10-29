package com.transport.sacco.Validator;

import com.transport.sacco.Entity.Sacco;
import com.transport.sacco.Repository.SaccoRepository;
import com.transport.liby.service.BaseJpaRepoReadService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

@Component
public class UniqueSaccoNameValidator extends BaseJpaRepoReadService<Sacco, SaccoRepository>implements ConstraintValidator<UniqueSaccoName, String> {
    @Override
    public boolean isValid(String saccoName, ConstraintValidatorContext context) {
        if (saccoName == null || saccoName.isBlank()) {
            return true;
        }

        var spec = repository.notDeleted()
                .and(repository.saccoNameIs(saccoName));

        return !repository.exists(spec);
    }
}
