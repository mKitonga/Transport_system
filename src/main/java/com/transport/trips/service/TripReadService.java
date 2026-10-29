package com.transport.trips.service;

import com.transport.school.SchoolAdmin.Service.SchoolAdminReadService;
import com.transport.trips.entity.Trip;
import com.transport.trips.form.FetchTripForm;
import com.transport.trips.repository.TripRepository;
import com.transport.liby.service.BaseJpaRepoReadService;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public class TripReadService extends BaseJpaRepoReadService<Trip, TripRepository> {

    private SchoolAdminReadService schoolAdminReadService;

    public Page<Trip> listTrips(FetchTripForm form) {
        var spec = repository.notDeleted();

        if (StringUtils.isNotBlank(form.getQuery())) {
            String query = "%" + form.getQuery().toLowerCase() + "%";
            spec = spec.and((root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("schoolName")), query),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("location")), query)
            ));
        }

        if (form.getSchoolGrade() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("grade"), form.getSchoolGrade()
            ));
        }

        if (form.getTripStatus() != null) {
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(
                    root.get("status"), form.getTripStatus()
            ));
        }

        if (form.getTripType() != null) {
            spec = spec.and(repository.tripTypeIs(form.getTripType()));
        }

        var schoolAdmin = schoolAdminReadService.findByEntityId(form.getSessionUserId());
        var school = schoolAdmin.getSchool();
        String schoolId = school == null ? null : school.getEntityId();
        spec = spec.and((root, query, criteriaBuilder) -> schoolId == null
                ? criteriaBuilder.disjunction()
                : criteriaBuilder.equal(
                        root.get("schoolAdmin").get("school").get("entityId"),
                        schoolId
                ));

        return repository.findAll(spec, repository.defaultPageable(form));
    }

    @Autowired
    public void setSchoolAdminReadService(SchoolAdminReadService schoolAdminReadService) {
        this.schoolAdminReadService = schoolAdminReadService;
    }
}
