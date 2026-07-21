package com.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.transport.entity.SaccoDriverProfile;

/**
 * Provides database access for {@link SaccoDriverProfile} records.
 */
public interface SaccoDriverProfileRepository extends JpaRepository<SaccoDriverProfile, Long> {
}
