package com.transport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.transport.entity.ParentProfile;

/**
 * Provides database access for {@link ParentProfile} records.
 */
public interface ParentProfileRepository extends JpaRepository<ParentProfile, Long> {
}
