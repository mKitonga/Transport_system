package com.transport.parents.repository;

import com.transport.parents.entity.Parent;
import com.transport.school.Entity.SchoolGrade;
import com.transport.user.repository.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;

@Repository
public interface ParentRepository extends UserRepository<Parent> {

    default Specification<Parent> schoolEntityIdIs(String schoolEntityId) {
        return (root, q, cb) -> cb.equal(root.join("school").get("entityId"), schoolEntityId);
    }

    default Specification<Parent> gradeIs(SchoolGrade grade) {
        return (root, q, cb) -> cb.equal(root.get("grade"), grade);
    }
}
