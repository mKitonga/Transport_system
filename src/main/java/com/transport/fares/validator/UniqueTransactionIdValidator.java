package com.transport.fares.validator;

import com.transport.fares.entity.FarePayment;
import com.transport.fares.repository.FarePaymentRepository;
import com.transport.liby.service.BaseJpaRepoReadService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class UniqueTransactionIdValidator extends BaseJpaRepoReadService<FarePayment, FarePaymentRepository>
        implements ConstraintValidator<UniqueTransactionId, String> {

    @Override
    public boolean isValid(String transactionId, ConstraintValidatorContext constraintValidatorContext) {
        if (transactionId == null || transactionId.isBlank()) {
            return true;
        }
        Specification<FarePayment> spec = repository.notDeleted().and(repository.transactionIdIs(transactionId));

        return !repository.exists(spec);
    }
}
