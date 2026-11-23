package com.transport.sacco.SaccoDriver.Repository;

import com.transport.liby.repository.BaseJpaRepository;
import com.transport.sacco.SaccoDriver.Entity.PassengerPayment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface PassengerPaymentRepository extends BaseJpaRepository<PassengerPayment> {

    default Specification<PassengerPayment> driverEntityIdIs(String driverEntityId) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.join("driver").get("entityId"), driverEntityId);
    }

    default Specification<PassengerPayment> unclearedOnly() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isNull(root.get("clearedAt"));
    }
}
