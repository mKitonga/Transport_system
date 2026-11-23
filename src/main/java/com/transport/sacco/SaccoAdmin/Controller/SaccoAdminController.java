package com.transport.sacco.SaccoAdmin.Controller;

import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.Message;
import com.transport.liby.view.EntityApiResponse;
import com.transport.liby.view.PagedEntityApiResponse;
import com.transport.sacco.SaccoAdmin.Service.SaccoAdminReadService;
import com.transport.sacco.SaccoAdmin.View.SaccoAdminOverviewView;
import com.transport.sacco.SaccoAdmin.View.SaccoAdminView;
import com.transport.sacco.SaccoDriver.Service.SaccoDriverEditService;
import com.transport.sacco.SaccoDriver.Service.SaccoDriverReadService;
import com.transport.sacco.SaccoDriver.View.SaccoDriverView;
import com.transport.sts.validator.RequiredStsUserStatus;
import com.transport.trips.service.TripEditService;
import com.transport.trips.view.TripView;
import com.transport.user.entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static com.transport.liby.service.SystemConfig.STS_USER_BASE_URL;

@RestController
@RequestMapping(STS_USER_BASE_URL + "/sacco-admin")
public class SaccoAdminController {

    private SaccoAdminReadService saccoAdminReadService;
    private SaccoDriverReadService saccoDriverReadService;
    private SaccoDriverEditService saccoDriverEditService;
    private TripEditService tripEditService;

    @PreAuthorize("hasAuthority('VIEW_SACCO_OVERVIEW')")
    @GetMapping("/overview")
    public EntityApiResponse<SaccoAdminOverviewView> getOverview(Authentication auth) {
        var overview = saccoAdminReadService.getOverview(auth.getName());
        return new EntityApiResponse<>(overview);
    }

    @GetMapping("/profile")
    public EntityApiResponse<SaccoAdminView> getProfile(Authentication auth) {
        var admin = saccoAdminReadService.findActiveAdminByEntityId(auth.getName())
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));
        return new EntityApiResponse<>(new SaccoAdminView(admin));
    }

    @PreAuthorize("hasAuthority('VIEW_DRIVERS')")
    @GetMapping("/sacco-drivers")
    public PagedEntityApiResponse<SaccoDriverView> listDrivers(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "status", required = false) UserStatus status,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {
        var admin = saccoAdminReadService.findActiveAdminByEntityId(auth.getName())
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        if (admin.getSacco() == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = saccoDriverReadService.listDrivers(form, admin.getSacco().getEntityId(), status);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @PreAuthorize("hasAuthority('APPROVE_DRIVER')")
    @PutMapping("/approve-drivers/{driverId}")
    public EntityApiResponse<SaccoDriverView> approveDriver(
            @PathVariable String driverId,
            @RequiredStsUserStatus(
                    statuses = {UserStatus.PENDING_APPROVAL, UserStatus.SUSPENDED},
                    message = "error.user.already.active")
            Authentication auth,
            Locale locale) {
        var admin = saccoAdminReadService.findActiveAdminByEntityId(auth.getName())
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        if (admin.getSacco() == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        var driver = saccoDriverEditService.approveDriver(driverId, admin.getSacco().getEntityId(), auth.getName());
        return new EntityApiResponse<>(
                Message.get("driver.approval.success", locale),
                new SaccoDriverView(driver)
        );
    }

    @PreAuthorize("hasAuthority('SUSPEND_DRIVER')")
    @PutMapping("/suspend-drivers/{driverId}")
    public EntityApiResponse<SaccoDriverView> suspendDriver(
            @PathVariable String driverId,
            @RequiredStsUserStatus(
                    statuses = {UserStatus.ACTIVE},
                    message = "error.user.not.active"
            )
            Authentication auth,
            Locale locale) {
        var admin = saccoAdminReadService.findActiveAdminByEntityId(auth.getName())
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        if (admin.getSacco() == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        var driver = saccoDriverEditService.suspendDriver(driverId, admin.getSacco().getEntityId(), auth.getName());
        return new EntityApiResponse<>(
                Message.get("driver.suspend.success", locale),
                new SaccoDriverView(driver)
        );
    }

    @PreAuthorize("hasAuthority('ASSIGN_DRIVER_TO_TRIP') or hasAuthority('SACCO_ADMIN')")
    @PutMapping("/trips/{tripId}/assign-driver/{driverId}")
    public EntityApiResponse<TripView> assignDriverToTrip(
            @PathVariable String tripId,
            @PathVariable String driverId,
            Authentication auth,
            Locale locale) {
        var admin = saccoAdminReadService.findActiveAdminByEntityId(auth.getName())
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        if (admin.getSacco() == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        var trip = tripEditService.assignDriverToTrip(tripId, driverId, admin.getSacco().getEntityId(), auth.getName());
        return new EntityApiResponse<>(
                Message.get("trip.driver.assignment.success", locale),
                TripView.toView(trip)
        );
    }

    @Autowired
    public void setSaccoAdminReadService(SaccoAdminReadService saccoAdminReadService) {this.saccoAdminReadService = saccoAdminReadService;}

    @Autowired
    public void setSaccoDriverReadService(SaccoDriverReadService saccoDriverReadService) {this.saccoDriverReadService = saccoDriverReadService;}

    @Autowired
    public void setSaccoDriverEditService(SaccoDriverEditService saccoDriverEditService) {this.saccoDriverEditService = saccoDriverEditService;}

    @Autowired
    public void setTripEditService(TripEditService tripEditService) {this.tripEditService = tripEditService;}
}
