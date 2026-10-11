package com.transport.Sacco.SaccoAdmin.Repository;

import com.transport.Sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.User.repository.UserRepository;

import java.util.List;
import java.util.Optional;

public interface SaccoAdminRepository extends UserRepository<SaccoAdmin> {

    Optional<SaccoAdmin> findByEntityIdAndDeletedAtIsNullAndDeletedByEntityIdIsNull(String entityId);

    List<SaccoAdmin> findAllByStation_EntityIdAndDeletedAtIsNullAndDeletedByEntityIdIsNull(String stationId);
}
