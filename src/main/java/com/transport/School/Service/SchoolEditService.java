package com.transport.School.Service;

import com.transport.School.Entity.School;
import com.transport.School.Form.RegisterSchoolForm;
import com.transport.School.Repository.SchoolRepository;
import com.transport.School.SchoolAdmin.Service.SchoolAdminEditService;
import com.transport.School.SchoolAdmin.Service.SchoolAdminReadService;
import com.transport.liby.service.BaseJpaRepoEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SchoolEditService extends BaseJpaRepoEditService<School, SchoolRepository> {

    private SchoolAdminReadService schoolAdminReadService;
    private SchoolAdminEditService schoolAdminEditService;

    public School registerSchool(RegisterSchoolForm form) {
        var school = new School();
        school.setName(form.getName());
        school.setLocation(form.getLocation());
        school.setCreatedByEntityId(form.getSessionUserId());

        school = save(school, form.getSessionUserId());

        // Auto-link the registering admin to this school
        School finalSchool = school;
        schoolAdminReadService.findActiveAdminByEntityId(form.getSessionUserId())
                .ifPresent(admin -> {
                    admin.setSchool(finalSchool);
                    schoolAdminEditService.save(admin, form.getSessionUserId());
                });

        return school;
    }

    @Autowired
    public void setSchoolAdminReadService(SchoolAdminReadService schoolAdminReadService) {
        this.schoolAdminReadService = schoolAdminReadService;
    }

    @Autowired
    public void setSchoolAdminEditService(SchoolAdminEditService schoolAdminEditService) {
        this.schoolAdminEditService = schoolAdminEditService;
    }
}
