package com.transport.payment.repository;

import com.transport.liby.repository.BaseJpaRepository;
import com.transport.payment.entity.MpesaC2BPayment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface MpesaC2BPaymentRepository extends BaseJpaRepository<MpesaC2BPayment> {
    default Specification<MpesaC2BPayment> transactionIdIs(String transactionId) {
        return (root, cq, cb) -> cb.equal(root.get("transactionId"), transactionId);
    }
}
