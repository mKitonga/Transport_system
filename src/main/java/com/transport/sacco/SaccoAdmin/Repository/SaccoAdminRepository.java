package com.transport.sacco.SaccoAdmin.Repository;

import com.transport.sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public interface SaccoAdminRepository extends UserRepository<SaccoAdmin> {

    Optional<SaccoAdmin> findByEntityIdAndDeletedAtIsNullAndDeletedByEntityIdIsNull(String entityId);

    List<SaccoAdmin> findAllByStation_EntityIdAndDeletedAtIsNullAndDeletedByEntityIdIsNull(String stationId);
}
