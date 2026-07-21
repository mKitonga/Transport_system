package com.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.transport.entity.SchoolAdminProfile;

/**
 * Provides database access for {@link SchoolAdminProfile} records.
 */
public interface SchoolAdminProfileRepository extends JpaRepository<SchoolAdminProfile, Long> {
}
