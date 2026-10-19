package com.transport.Routes.Service;

import com.transport.Routes.Entity.Route;
import com.transport.Routes.Form.EditRouteForm;
import com.transport.Routes.Form.RegisterRouteForm;
import com.transport.Routes.Form.UpdateRouteTerminalsForm;
import com.transport.Routes.Repository.RouteRepository;
import com.transport.Sacco.SaccoAdmin.Service.SaccoAdminReadService;
import com.transport.Sacco.Entity.Sacco;
import com.transport.Sacco.SaccoDriver.Entity.SaccoDriver;
import com.transport.Sacco.SaccoDriver.Service.SaccoDriverEditService;
import com.transport.Sacco.Service.SaccoReadService;
import com.transport.School.SchoolAdmin.Service.SchoolAdminReadService;
import com.transport.User.entity.UserType;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;

@Service
public class RouteEditService extends BaseJpaRepoEditService<Route, RouteRepository> {

    private TerminalReadService terminalReadService;
    private SaccoAdminReadService saccoAdminReadService;
    private SaccoReadService saccoReadService;
    private SchoolAdminReadService schoolAdminReadService;
    private SaccoDriverEditService saccoDriverEditService;

    @Transactional
    public Route registerRoute(RegisterRouteForm form, UserType userType) {
        Route route = new Route();
        route.setName(form.getName().trim());
        route.setCreatedByEntityId(form.getSessionUserId());
        if (userType == UserType.SCHOOL_ADMIN) {
            var schoolAdmin = schoolAdminReadService.findActiveAdminByEntityId(form.getSessionUserId())
                    .orElseThrow(this::notFound);
            if (schoolAdmin.getSchool() == null) {
                throw notFound();
            }
            route.setSchool(schoolAdmin.getSchool());
        } else if (userType == UserType.SACCO_ADMIN) {
            var saccoAdmin = saccoAdminReadService.findActiveAdminByEntityId(form.getSessionUserId());
            var sacco = saccoAdmin.map(admin -> admin.getSacco())
                    .orElseGet(() -> singleOwnedSacco(form.getSessionUserId()));
            if (sacco == null) {
                throw notFound();
            }
            route.setSacco(sacco);
        } else {
            throw notFound();
        }

        route = save(route, form.getSessionUserId());

        return route;
    }

    @Transactional
    public Route editRoute(String routeId, EditRouteForm form, String adminId) {
        Route route = findOwnedRoute(routeId, adminId);
        route.setName(form.getName().trim());

        route = save(route, form.getSessionUserId());

        return route;
    }

    @Transactional
    public Route updateRouteTerminals(String routeId, UpdateRouteTerminalsForm form,
            String adminId) {
        Route route = findOwnedRoute(routeId, adminId);
        var terminalIds = form.getTerminalIds();
        var terminals = terminalReadService.findByIds(new ArrayList<>(terminalIds));

        if (terminals.size() != terminalIds.size()) {
            throw notFound();
        }

        route.getTerminals().clear();
        route.getTerminals().addAll(new HashSet<>(terminals));

        route = save(route, form.getSessionUserId());
        return route;
    }

    @Transactional
    public SaccoDriver assignRouteToDriver(String routeId, String driverId, String adminId) {
        Route route = findOwnedRoute(routeId, adminId);
        var saccoAdmin = saccoAdminReadService.findActiveAdminByEntityId(adminId)
                .orElseThrow(this::notFound);
        var station = saccoAdmin.getStation();
        if (station == null
                || route.getSacco() == null
                || saccoAdmin.getSacco() == null
                || !route.getSacco().getEntityId().equals(saccoAdmin.getSacco().getEntityId())
                || route.getTerminals().stream().noneMatch(
                        terminal -> terminal.getEntityId().equals(station.getEntityId()))) {
            throw notFound();
        }

        return saccoDriverEditService.assignRoute(driverId, route, adminId);
    }

    @Transactional
    public void deleteRoute(String routeId, String adminId) {
        Route route = findOwnedRoute(routeId, adminId);
        delete(route, adminId);
    }

    @Transactional(readOnly = true)
    public Route findOwnedRoute(String routeId, String adminId) {
        Route route = findByEntityId(routeId);
        var saccoAdmin = saccoAdminReadService.findActiveAdminByEntityId(adminId);
        var schoolAdmin = schoolAdminReadService.findActiveAdminByEntityId(adminId);
        var saccoOwner = saccoAdmin.map(admin -> admin.getSacco()).orElseGet(() -> singleOwnedSacco(adminId));
        var schoolOwner = schoolAdmin.map(admin -> admin.getSchool()).orElse(null);
        boolean ownsSacco = saccoOwner != null;
        boolean ownsSchool = schoolOwner != null;
        if (ownsSacco == ownsSchool) {
            throw notFound();
        }
        boolean ownedByAdmin = ownsSacco
                ? route.getSacco() != null && saccoOwner.getEntityId().equals(route.getSacco().getEntityId())
                : route.getSchool() != null && schoolOwner.getEntityId().equals(route.getSchool().getEntityId());
        if (!ownedByAdmin) {
            throw notFound();
        }
        return route;
    }

    private CommonRuntimeException notFound() {
        return new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
    }

    private Sacco singleOwnedSacco(String adminId) {
        var ownedSaccos = saccoReadService.findOwnedByAdminId(adminId);
        return ownedSaccos.size() == 1 ? ownedSaccos.get(0) : null;
    }


    @Autowired
    public void setTerminalReadService(TerminalReadService terminalReadService) {
        this.terminalReadService = terminalReadService;
    }

    @Autowired
    public void setSaccoAdminReadService(SaccoAdminReadService saccoAdminReadService) {
        this.saccoAdminReadService = saccoAdminReadService;
    }

    @Autowired
    public void setSaccoReadService(SaccoReadService saccoReadService) {
        this.saccoReadService = saccoReadService;
    }

    @Autowired
    public void setSchoolAdminReadService(SchoolAdminReadService schoolAdminReadService) {
        this.schoolAdminReadService = schoolAdminReadService;
    }

    @Autowired
    public void setSaccoDriverEditService(SaccoDriverEditService saccoDriverEditService) {
        this.saccoDriverEditService = saccoDriverEditService;
    }
}
