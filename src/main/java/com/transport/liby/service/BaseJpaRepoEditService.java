package com.transport.liby.service;

import com.transport.liby.entity.BaseJpaEntity;
import com.transport.liby.repository.BaseJpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public abstract class BaseJpaRepoEditService<T extends BaseJpaEntity, R extends BaseJpaRepository<T>> extends BaseJpaRepoReadService<T, R> {

    public T save(T model, String updateById) {
        model.setUpdatedByEntityId(updateById);
        return save(model);
    }

    public T save(T model) {
        model.setUpdatedAt(LocalDateTime.now());
        return repository.save(model);
    }

    public List<T> save(List<T> models, String updateById) {
        models.forEach(
                model -> {
                    model.setUpdatedByEntityId(updateById);
                    model.setUpdatedAt(LocalDateTime.now());
                });
        return repository.saveAll(models);
    }


    public void delete(T model, String deletedById) {
        model.setDeletedAt(LocalDateTime.now());
        model.setDeletedByEntityId(deletedById);
        save(model);
    }
}
