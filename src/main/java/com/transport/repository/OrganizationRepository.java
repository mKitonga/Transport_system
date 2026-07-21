package com.transport.repository;

import com.transport.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link Organization} entities.
 */
public interface OrganizationRepository extends JpaRepository<Organization, Long> {
    // Custom query methods can be defined here
}
