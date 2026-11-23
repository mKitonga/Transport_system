package com.transport.sacco.Repository;

import com.transport.liby.repository.BaseJpaRepository;
import com.transport.sacco.Entity.SaccoTransportRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface SaccoTransportRequestRepository extends BaseJpaRepository<SaccoTransportRequest> {

    default Specification<SaccoTransportRequest> schoolAdminEntityIdIs(String adminEntityId) {
        return (root, q, cb) -> cb.equal(root.join("schoolAdmin").get("entityId"), adminEntityId);
    }

    default Specification<SaccoTransportRequest> saccoAdminEntityIdIs(String adminEntityId) {
        return (root, q, cb) -> cb.equal(root.join("saccoAdmin").get("entityId"), adminEntityId);
    }

    default Specification<SaccoTransportRequest> statusIs(String status) {
        return (root, q, cb) -> cb.equal(root.get("status"), status);
    }
}
