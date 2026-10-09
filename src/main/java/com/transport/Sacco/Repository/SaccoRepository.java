package com.transport.Sacco.Repository;

import com.transport.Sacco.Entity.Sacco;
import com.transport.liby.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;

public interface SaccoRepository extends BaseJpaRepository<Sacco> {
    default Specification<Sacco> createdOrUpdatedByEntityIdIs(String adminId) {
        return (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
                criteriaBuilder.equal(root.get("createdByEntityId"), adminId),
                criteriaBuilder.equal(root.get("updatedByEntityId"), adminId)
        );
    }

    default Specification<Sacco> nameLike(String query) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + query.toLowerCase() + "%"
                );
    }

        default Specification<Sacco> saccoNameIs(String saccoName) {
            return (root, criteriaQuery, criteriaBuilder) ->
                    criteriaBuilder.equal(
                            root.get("name"), saccoName);
        }
}

