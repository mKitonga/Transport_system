package com.transport.user.service;

import com.transport.authentication.form.RegisterParentForm;
import com.transport.authentication.form.RegisterSaccoAdminForm;
import com.transport.authentication.form.RegisterSaccoDriverForm;
import com.transport.authentication.form.RegisterSchoolAdminForm;
import com.transport.authentication.form.RegisterSchoolDriverForm;
import com.transport.authentication.view.AuthView;
import com.transport.parents.entity.Parent;
import com.transport.routes.Service.RouteReadService;
import com.transport.routes.Service.TerminalReadService;
import com.transport.sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.sacco.SaccoDriver.Entity.SaccoDriver;
import com.transport.sacco.Service.SaccoReadService;
import com.transport.school.Entity.School;
import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.school.SchoolDriver.Entity.SchoolDriver;
import com.transport.school.Service.SchoolReadService;
import com.transport.user.entity.UserType;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserRegistrationService {
    private final TransportUserAuthService userAuthService;
    private final SchoolReadService schoolReadService;
    private final SaccoReadService saccoReadService;
    private final TerminalReadService terminalReadService;
    private final RouteReadService routeReadService;

    public UserRegistrationService(
            TransportUserAuthService userAuthService,
            SchoolReadService schoolReadService,
            SaccoReadService saccoReadService,
            TerminalReadService terminalReadService,
            RouteReadService routeReadService) {
        this.userAuthService = userAuthService;
        this.schoolReadService = schoolReadService;
        this.saccoReadService = saccoReadService;
        this.terminalReadService = terminalReadService;
        this.routeReadService = routeReadService;
    }

    @Transactional
    public AuthView registerParent(RegisterParentForm form) {
        School school = schoolReadService.findByEntityId(form.getSchoolEntityId());
        Parent parent = new Parent();
        parent.setStudentName(form.getStudentName());
        parent.setAdmissionNumber(form.getAdmissionNumber());
        parent.setGrade(form.getGrade());
        parent.setSchool(school);
        parent.setSchoolName(school.getName());
        parent.setSchoolLocation(school.getLocation());
        return userAuthService.registerUser(parent, form, UserType.PARENT);
    }

    @Transactional
    public AuthView registerSchoolAdmin(RegisterSchoolAdminForm form) {
        SchoolAdmin schoolAdmin = new SchoolAdmin();
        if (form.getSchoolEntityId() != null && !form.getSchoolEntityId().isBlank()) {
            schoolAdmin.setSchool(schoolReadService.findByEntityId(form.getSchoolEntityId()));
        }
        schoolAdmin.setLocation(form.getLocation());
        return userAuthService.registerUser(schoolAdmin, form, UserType.SCHOOL_ADMIN);
    }

    @Transactional
    public AuthView registerSchoolDriver(RegisterSchoolDriverForm form) {
        SchoolDriver schoolDriver = new SchoolDriver();
        schoolDriver.setSchool(schoolReadService.findByEntityId(form.getSchoolEntityId()));
        schoolDriver.setLocation(form.getLocation());
        return userAuthService.registerUser(schoolDriver, form, UserType.SCHOOL_DRIVER);
    }

    @Transactional
    public AuthView registerSaccoAdmin(RegisterSaccoAdminForm form) {
        SaccoAdmin saccoAdmin = new SaccoAdmin();
        saccoAdmin.setSacco(saccoReadService.findByEntityId(form.getSaccoEntityId()));
        saccoAdmin.setStation(terminalReadService.findByEntityId(form.getStationEntityId()));
        return userAuthService.registerUser(saccoAdmin, form, UserType.SACCO_ADMIN);
    }

    @Transactional
    public AuthView registerSaccoDriver(RegisterSaccoDriverForm form) {
        SaccoDriver saccoDriver = new SaccoDriver();

        if (form.getSaccoEntityId() != null && !form.getSaccoEntityId().isBlank()) {
            saccoDriver.setSacco(saccoReadService.findByEntityId(form.getSaccoEntityId()));
        }

        if (form.getAssignedRouteEntityId() != null && !form.getAssignedRouteEntityId().isBlank()) {
            var route = routeReadService.findByEntityId(form.getAssignedRouteEntityId());
            if (route.getSacco() == null
                    || (saccoDriver.getSacco() != null
                    && !route.getSacco().getEntityId().equals(saccoDriver.getSacco().getEntityId()))) {
                throw new CommonRuntimeException(
                        ExceptionType.BAD_REQUEST,
                        "error.route.does.not.belong.to.sacco"
                );
            }
            saccoDriver.setAssignedRoute(route);
            if (saccoDriver.getSacco() == null) {
                saccoDriver.setSacco(route.getSacco());
            }
        }

        saccoDriver.setNumberPlate(form.getNumberPlate());
        saccoDriver.setMatatuName(form.getMatatuName());
        return userAuthService.registerUser(saccoDriver, form, UserType.SACCO_DRIVER);
    }
}
