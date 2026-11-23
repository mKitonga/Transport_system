package com.transport.fares.repository;

import com.transport.fares.entity.FarePayment;
import com.transport.liby.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FarePaymentRepository extends BaseJpaRepository<FarePayment> {

    Optional<FarePayment> findByTransactionId(String transactionId);

    default Specification<FarePayment> transactionIdIs(String transactionId) {
        return (root, query, cb) -> cb.equal(root.get("transactionId"), transactionId);
    }
}
