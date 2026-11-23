package com.transport.sacco.SaccoDriver.Repository;

import com.transport.liby.repository.BaseJpaRepository;
import com.transport.sacco.SaccoDriver.Entity.SchoolTransportNotification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolTransportNotificationRepository extends BaseJpaRepository<SchoolTransportNotification> {

    default Specification<SchoolTransportNotification> schoolAdminEntityIdIs(String adminEntityId) {
        return (root, q, cb) -> cb.equal(root.join("schoolAdmin").get("entityId"), adminEntityId);
    }

    default Specification<SchoolTransportNotification> statusIs(String status) {
        return (root, q, cb) -> cb.equal(root.get("status"), status);
    }
}
