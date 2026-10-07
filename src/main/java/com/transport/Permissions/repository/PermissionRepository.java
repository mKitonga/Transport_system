package com.transport.Permissions.repository;

import com.transport.Permissions.entity.Permission;
import com.transport.liby.repository.BaseJpaRepository;
import org.springframework.data.jpa.domain.Specification;

public interface PermissionRepository extends BaseJpaRepository<Permission> {
    default Specification<Permission> permissionNameLike(String keyWord){
        return (root, cb, cq) -> cq.like(root.get("permissionName"), "%"+keyWord+"%");
    }

    default Specification<Permission> descriptionLike(String keyWord){
        return (root, cb, cq) -> cq.like(root.get("description"), "%"+keyWord+"%");
    }
}
