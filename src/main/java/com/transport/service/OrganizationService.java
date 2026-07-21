package com.transport.service;

import com.transport.entity.Organization;

import java.util.List;

/**
 * Service interface defining business operations for {@link Organization} management.
 */
public interface OrganizationService {

    Organization createOrganization(Organization organization);

    Organization getOrganizationById(Long id);

    List<Organization> getAllOrganizations();

    Organization updateOrganization(Long id, Organization organization);

    void deleteOrganization(Long id);
}
