package com.transport.school.SchoolDriver.Repository;

import com.transport.school.SchoolDriver.Entity.SchoolDriver;
import com.transport.user.repository.UserRepository;
import org.springframework.data.jpa.domain.Specification;

public interface SchoolDriverRepository extends UserRepository<SchoolDriver> {

    default Specification<SchoolDriver> schoolEntityIdIs(String schoolEntityId) {
        return (root, q, cb) -> cb.equal(root.join("school").get("entityId"), schoolEntityId);
    }
}
