package com.transport.service.impl;

import com.transport.entity.Organization;
import com.transport.repository.OrganizationRepository;
import com.transport.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of {@link OrganizationService} providing business logic for organization management.
 */
@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    @Transactional
    public Organization createOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Override
    public Organization getOrganizationById(Long id) {
        return organizationRepository.findById(id).orElse(null);
    }

    @Override
    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    @Override
    @Transactional
    public Organization updateOrganization(Long id, Organization organization) {
        Organization existingOrganization = organizationRepository.findById(id).orElse(null);
        if (existingOrganization != null) {
            existingOrganization.setName(organization.getName());
            existingOrganization.setType(organization.getType());
            return organizationRepository.save(existingOrganization);
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteOrganization(Long id) {
        organizationRepository.deleteById(id);
    }
}
