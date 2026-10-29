package com.transport.sacco.SaccoDriver.Repository;

import com.transport.sacco.SaccoDriver.Entity.SaccoDriver;
import com.transport.user.repository.UserRepository;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SaccoDriverRepository extends UserRepository<SaccoDriver> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
            select d from SaccoDriver d
            where d.entityId = :driverId
              and d.deletedAt is null
              and d.deletedByEntityId is null
            """)
    Optional<SaccoDriver> findActiveByEntityIdForUpdate(@Param("driverId") String driverId);
}
