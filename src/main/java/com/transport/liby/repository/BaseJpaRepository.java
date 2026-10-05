package com.transport.liby.repository;

import com.transport.liby.entity.BaseJpaEntity;
import com.transport.liby.form.BaseFetchForm;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@NoRepositoryBean
public interface BaseJpaRepository<T extends BaseJpaEntity> extends JpaRepository<T, Long>, JpaSpecificationExecutor<T> {

    boolean existsByEntityId(String entityId);

    Optional<T> findByEntityIdAndDeletedAtIsNull(String entityId);

    Optional<T> findByEntityId(String entityId);

    default Specification<T> entityIdIn(List<String> entityIds) {
        return (root, query, builder) -> root.get("entityId").in(entityIds);
    }

    default Specification<T> createAtBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return (root, query, builder) -> builder.between(root.get("createdAt"), startDate, endDate);
    }

    default Specification<T> notDeleted() {
        return (root, query, builder) -> {
            List<Predicate> predicates = List.of(
                    builder.isNull(root.get("deletedAt")),
                    builder.isNull(root.get("deletedByEntityId"))
            );
            return builder.or(predicates.toArray(Predicate[]::new));
        };
    }

    default Specification<T> entityIdIs(String entityId) {
        return (root, cb, cq) -> cq.equal(root.get("entityId"), entityId);
    }

    default Specification<T> entityIdNot(String entityId) {
        return (root, cb, cq) -> cq.notEqual(root.get("entityId"), entityId);
    }

    Sort CREATED_AT_DESC = Sort.by(Sort.Direction.DESC, "createdAt");

    default Pageable defaultPageable(BaseFetchForm form, Sort sort) {
        if (sort == null) {
            return defaultPageable(form);
        } else {
            return PageRequest.of(
                    form.getPageNum(),
                    form.getPageSize(),
                    sort
            );
        }
    }

    default Pageable defaultPageable(BaseFetchForm form) {
        return PageRequest.of(
                form.getPageNum(),
                form.getPageSize(),
                CREATED_AT_DESC
        );
    }
}
