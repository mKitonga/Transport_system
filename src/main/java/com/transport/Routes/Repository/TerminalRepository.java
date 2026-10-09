package com.transport.Routes.Repository;

import com.transport.Routes.Entity.Terminal;
import com.transport.liby.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;

public interface TerminalRepository extends BaseJpaRepository<Terminal> {

    default Specification<Terminal> nameLike(String query) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + query.toLowerCase() + "%"
                );
    }
}
