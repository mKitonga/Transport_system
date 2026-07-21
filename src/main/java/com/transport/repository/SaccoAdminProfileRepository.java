package com.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.transport.entity.SaccoAdminProfile;

/**
 * Provides database access for {@link SaccoAdminProfile} records.
 */
public interface SaccoAdminProfileRepository extends JpaRepository<SaccoAdminProfile, Long> {
}
