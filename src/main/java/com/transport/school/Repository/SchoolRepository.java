package com.transport.school.Repository;

import com.transport.school.Entity.School;
import com.transport.liby.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;

public interface SchoolRepository extends BaseJpaRepository<School> {
    default Specification<School> nameLike(String query) {
        return(root,cq,cb) -> cb.like(
                cb.lower(root.get("name")),
                "%" + query.toLowerCase() + "%"
        );
    }
}
