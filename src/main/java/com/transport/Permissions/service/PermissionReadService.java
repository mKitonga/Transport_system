package com.transport.Permissions.service;

import com.transport.Permissions.entity.Permission;
import com.transport.Permissions.repository.PermissionRepository;
import com.transport.User.entity.UserType;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.BaseJpaRepoReadService;
import io.micrometer.common.util.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class PermissionReadService extends BaseJpaRepoReadService<Permission, PermissionRepository> {

    public List<String> findPermissionNamesForUserTypes(Collection<UserType> userTypes) {
        return repository.findPermissionNamesForUserTypes(userTypes);
    }

    public Page<Permission> listUserPermissions(BaseFetchForm form){
        Specification<Permission> spec = repository.notDeleted();

        if(StringUtils.isNotEmpty(form.getQuery())) {
            spec = spec.and(
                    repository.permissionNameLike(form.getQuery())
                            .or(repository.descriptionLike(form.getQuery()))
            );
        }

        var pageable = repository.defaultPageable(form);
        return repository.findAll(spec, pageable);
    }
}

