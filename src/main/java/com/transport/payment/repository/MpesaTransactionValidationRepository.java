package com.transport.payment.repository;

import com.transport.liby.repository.BaseJpaRepository;
import com.transport.payment.entity.MpesaTransactionValidation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface MpesaTransactionValidationRepository extends BaseJpaRepository<MpesaTransactionValidation> {

    default Specification<MpesaTransactionValidation> originatorConversationIdIs(String id){
        return ((root, cq, cb) -> cb.equal(root.get("originatorConversationId"), id));
    }
}
