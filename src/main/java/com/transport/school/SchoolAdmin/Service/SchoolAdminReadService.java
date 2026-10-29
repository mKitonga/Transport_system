package com.transport.school.SchoolAdmin.Service;

import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.school.SchoolAdmin.Repository.SchoolAdminRepository;
import com.transport.liby.service.BaseJpaRepoReadService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SchoolAdminReadService extends BaseJpaRepoReadService<SchoolAdmin, SchoolAdminRepository> {

    public Optional<SchoolAdmin> findActiveAdminByEntityId(String entityId) {
        return repository.findByEntityIdAndDeletedAtIsNull(entityId);
    }
}
