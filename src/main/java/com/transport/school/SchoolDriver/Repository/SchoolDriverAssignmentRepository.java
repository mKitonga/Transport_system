package com.transport.school.SchoolDriver.Repository;

import com.transport.liby.repository.BaseJpaRepository;
import com.transport.school.SchoolDriver.Entity.SchoolDriverAssignment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SchoolDriverAssignmentRepository extends BaseJpaRepository<SchoolDriverAssignment> {

    Optional<SchoolDriverAssignment> findByDailyPickupTrip_EntityIdAndParent_EntityId(String tripId, String parentId);

    default Specification<SchoolDriverAssignment> schoolDriverEntityIdIs(String driverEntityId) {
        return (root, q, cb) -> cb.equal(root.join("schoolDriver").get("entityId"), driverEntityId);
    }

    default Specification<SchoolDriverAssignment> tripEntityIdIs(String tripEntityId) {
        return (root, q, cb) -> cb.equal(root.join("dailyPickupTrip").get("entityId"), tripEntityId);
    }
}
