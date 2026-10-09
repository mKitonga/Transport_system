package com.transport.Sacco.SaccoAdmin.Service;

import com.transport.Sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.Sacco.SaccoAdmin.Repository.SaccoAdminRepository;
import com.transport.liby.service.BaseJpaRepoReadService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SaccoAdminReadService extends BaseJpaRepoReadService<SaccoAdmin, SaccoAdminRepository> {

    public Optional<SaccoAdmin> findActiveAdminByEntityId(String entityId) {
        return repository.findByEntityIdAndDeletedAtIsNull(entityId);
    }
}
