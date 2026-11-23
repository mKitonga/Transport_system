package com.transport.school.SchoolAdmin.Repository;

import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.user.repository.UserRepository;
import org.springframework.data.jpa.domain.Specification;

public interface SchoolAdminRepository extends UserRepository<SchoolAdmin> {

    default Specification<SchoolAdmin> schoolEntityIdIs(String schoolEntityId) {
        return (root, q, cb) -> cb.equal(root.join("school").get("entityId"), schoolEntityId);
    }
}
