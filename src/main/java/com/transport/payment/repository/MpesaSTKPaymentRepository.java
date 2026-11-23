package com.transport.payment.repository;

import com.transport.liby.repository.BaseJpaRepository;
import com.transport.payment.entity.MpesaSTKPayment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface MpesaSTKPaymentRepository extends BaseJpaRepository<MpesaSTKPayment> {
    default Specification<MpesaSTKPayment> checkoutRequestIdIs(String checkoutRequestId){
        return (root, cq, cb) -> cb.equal(root.get("checkoutRequestId"), checkoutRequestId);
    }
    default Specification<MpesaSTKPayment> accountReferenceIs(String accountReference) {
        return (root, cq, cb) -> cb.equal(root.get("accountReference"), accountReference);
    }
}
