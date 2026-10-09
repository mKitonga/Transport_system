package com.transport.School.SchoolAdmin.Service;

import com.transport.School.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.School.SchoolAdmin.Repository.SchoolAdminRepository;
import com.transport.liby.service.BaseJpaRepoReadService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SchoolAdminReadService extends BaseJpaRepoReadService<SchoolAdmin, SchoolAdminRepository> {

    public Optional<SchoolAdmin> findActiveAdminByEntityId(String entityId) {
        return repository.findByEntityIdAndDeletedAtIsNull(entityId);
    }
}
