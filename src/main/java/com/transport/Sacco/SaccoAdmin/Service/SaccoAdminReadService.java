package com.transport.Sacco.SaccoAdmin.Service;

import com.transport.Sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.Sacco.SaccoAdmin.Repository.SaccoAdminRepository;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoReadService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SaccoAdminReadService extends BaseJpaRepoReadService<SaccoAdmin, SaccoAdminRepository> {

    public Optional<SaccoAdmin> findActiveAdminByEntityId(String entityId) {
        return repository.findByEntityIdAndDeletedAtIsNullAndDeletedByEntityIdIsNull(entityId);
    }

    public SaccoAdmin findActiveAdminByStationId(String stationId) {
        var admins = repository.findAllByStation_EntityIdAndDeletedAtIsNullAndDeletedByEntityIdIsNull(stationId);
        if (admins.size() != 1) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }
        return admins.get(0);
    }
}
