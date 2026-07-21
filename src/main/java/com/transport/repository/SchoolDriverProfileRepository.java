package com.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.transport.entity.SchoolDriverProfile;

/**
 * Provides database access for {@link SchoolDriverProfile} records.
 */
public interface SchoolDriverProfileRepository extends JpaRepository<SchoolDriverProfile, Long> {
}
