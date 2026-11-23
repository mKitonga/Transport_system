package com.transport.school.SchoolDriver.Repository;

import com.transport.liby.repository.BaseJpaRepository;
import com.transport.school.SchoolDriver.Entity.SchoolDriverNotification;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolDriverNotificationRepository extends BaseJpaRepository<SchoolDriverNotification> {

    default Specification<SchoolDriverNotification> schoolDriverEntityIdIs(String driverEntityId) {
        return (root, q, cb) -> cb.equal(root.join("schoolDriver").get("entityId"), driverEntityId);
    }

    default Specification<SchoolDriverNotification> schoolAdminEntityIdIs(String adminEntityId) {
        return (root, q, cb) -> cb.equal(root.join("schoolAdmin").get("entityId"), adminEntityId);
    }

    default Specification<SchoolDriverNotification> statusIs(String status) {
        return (root, q, cb) -> cb.equal(root.get("status"), status);
    }
}
