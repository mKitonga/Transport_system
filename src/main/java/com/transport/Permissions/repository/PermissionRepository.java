package com.transport.Permissions.repository;

import com.transport.Permissions.entity.Permission;
import com.transport.User.entity.UserType;
import com.transport.liby.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.domain.Specification;

import java.util.Collection;
import java.util.List;

public interface PermissionRepository extends BaseJpaRepository<Permission> {
    @Query("select distinct p.permissionName from Permission p join p.userTypes userType " +
            "where userType in :userTypes and p.deletedAt is null and p.deletedByEntityId is null")
    List<String> findPermissionNamesForUserTypes(@Param("userTypes") Collection<UserType> userTypes);

    default Specification<Permission> permissionNameLike(String keyWord){
        return (root, cb, cq) -> cq.like(root.get("permissionName"), "%"+keyWord+"%");
    }

    default Specification<Permission> descriptionLike(String keyWord){
        return (root, cb, cq) -> cq.like(root.get("description"), "%"+keyWord+"%");
    }
}
