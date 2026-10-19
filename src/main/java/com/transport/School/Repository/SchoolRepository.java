package com.transport.School.Repository;

import com.transport.School.Entity.School;
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
