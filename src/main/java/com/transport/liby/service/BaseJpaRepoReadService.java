package com.transport.liby.service;

import com.transport.liby.entity.BaseJpaEntity;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.repository.BaseJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.Optional;

public class BaseJpaRepoReadService <T extends BaseJpaEntity, R extends BaseJpaRepository<T>> {
    protected R repository;

    public T findByEntityId(String entityId) {
        Optional<T> opt = repository.findByEntityIdAndDeletedAtIsNull(entityId);
        if (opt.isEmpty()) {
            throw new CommonRuntimeException(
                    ExceptionType.NOT_FOUND,
                    "error.entity.not.found"
            );
        }
        return opt.get();
    }

    public boolean existsByEntityId(String entityId) {
        Specification<T> spec = repository.notDeleted()
                .and(repository.entityIdIs(entityId));
        return repository.exists(spec);
    }

    public T findByEntityIdIncludeDeleted(String entityId) {
        Optional<T> opt = repository.findByEntityId(entityId);
        if (opt.isEmpty()) {
            throw new CommonRuntimeException(
                    ExceptionType.NOT_FOUND,
                    "error.entity.not.found"
            );
        }
        return opt.get();
    }

    public List<T> findAll() {
        Specification<T> spec = repository.notDeleted();
        return repository.findAll(spec);
    }

    public List<T> findByIds(List<String> entityIds) {
        Specification<T> spec = repository.notDeleted()
                .and(repository.entityIdIn(entityIds));
        return repository.findAll(spec);
    }

    @Autowired
    public void setRepository(R repository) {
        this.repository = repository;
    }
}
