package com.transport.school.Repository;

import com.transport.liby.repository.BaseJpaRepository;
import com.transport.school.Entity.SchoolTripPayment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolTripPaymentRepository extends BaseJpaRepository<SchoolTripPayment> {

    Optional<SchoolTripPayment> findByParent_EntityIdAndTripTypeAndTripId(String parentId, String tripType, String tripId);

    List<SchoolTripPayment> findAllByParent_EntityId(String parentId);

    default Specification<SchoolTripPayment> parentEntityIdIs(String parentId) {
        return (root, q, cb) -> cb.equal(root.join("parent").get("entityId"), parentId);
    }

    default Specification<SchoolTripPayment> schoolAdminEntityIdIs(String adminId) {
        return (root, q, cb) -> cb.equal(root.join("schoolAdmin").get("entityId"), adminId);
    }

    default Specification<SchoolTripPayment> statusIs(String status) {
        return (root, q, cb) -> cb.equal(root.get("status"), status);
    }

    default Specification<SchoolTripPayment> tripTypeIs(String tripType) {
        return (root, q, cb) -> cb.equal(root.get("tripType"), tripType);
    }
}
