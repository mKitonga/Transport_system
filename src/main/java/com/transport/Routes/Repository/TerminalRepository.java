package com.transport.Routes.Repository;

import com.transport.Routes.Entity.Terminal;
import com.transport.liby.repository.BaseJpaRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TerminalRepository extends BaseJpaRepository<Terminal> {

    default Specification<Terminal> nameLike(String query) {
        return (root, criteriaQuery, criteriaBuilder) ->
                criteriaBuilder.like(
                        criteriaBuilder.lower(root.get("name")),
                        "%" + query.toLowerCase() + "%"
                );
    }

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select t from Terminal t
            where t.entityId = :terminalId
              and t.deletedAt is null
              and t.deletedByEntityId is null
            """)
    Optional<Terminal> findActiveByEntityIdForUpdate(@Param("terminalId") String terminalId);
}
