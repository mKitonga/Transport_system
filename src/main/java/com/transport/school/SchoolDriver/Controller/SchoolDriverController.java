package com.transport.school.SchoolDriver.Controller;

import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.Message;
import com.transport.liby.view.EntityApiResponse;
import com.transport.liby.view.PagedEntityApiResponse;
import com.transport.school.SchoolAdmin.View.SchoolDriverView;
import com.transport.school.SchoolDriver.Form.SendSchoolDriverAlertForm;
import com.transport.school.SchoolDriver.Service.SchoolDriverEditService;
import com.transport.school.SchoolDriver.Service.SchoolDriverReadService;
import com.transport.school.SchoolDriver.View.SchoolDriverAssignmentView;
import com.transport.school.SchoolDriver.View.SchoolDriverNotificationView;
import com.transport.school.SchoolDriver.View.SchoolDriverOverviewView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

import static com.transport.liby.service.SystemConfig.STS_USER_BASE_URL;

@RestController
@RequestMapping(STS_USER_BASE_URL + "/school-driver")
public class SchoolDriverController {

    private SchoolDriverReadService schoolDriverReadService;
    private SchoolDriverEditService schoolDriverEditService;

    @PreAuthorize("hasAuthority('SCHOOL_DRIVER')")
    @GetMapping("/overview")
    public EntityApiResponse<SchoolDriverOverviewView> getOverview(Authentication auth) {
        var overview = schoolDriverReadService.getOverview(auth.getName());
        return new EntityApiResponse<>(overview);
    }

    @GetMapping("/profile")
    public EntityApiResponse<SchoolDriverView> getProfile(Authentication auth) {
        var driver = schoolDriverReadService.findActiveByEntityId(auth.getName())
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));
        return new EntityApiResponse<>(new SchoolDriverView(driver));
    }

    @PreAuthorize("hasAuthority('VIEW_SCHOOL_ASSIGNMENTS')")
    @GetMapping("/assignments")
    public PagedEntityApiResponse<SchoolDriverAssignmentView> listAssignments(
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {
        var form = new BaseFetchForm();
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = schoolDriverReadService.listAssignments(auth.getName(), form);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @PreAuthorize("hasAuthority('VIEW_SCHOOL_TRIPS')")
    @GetMapping("/trips")
    public PagedEntityApiResponse<SchoolDriverAssignmentView> listTrips(
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {
        return listAssignments(pageNum, pageSize, auth);
    }

    @PreAuthorize("hasAuthority('SEND_ALERTS')")
    @GetMapping("/alerts")
    public PagedEntityApiResponse<SchoolDriverNotificationView> listAlerts(
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {
        var form = new BaseFetchForm();
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = schoolDriverReadService.listMyAlerts(auth.getName(), form);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @PreAuthorize("hasAuthority('SEND_ALERTS')")
    @PostMapping("/alerts")
    public EntityApiResponse<List<SchoolDriverNotificationView>> sendAlert(
            @Valid @RequestBody SendSchoolDriverAlertForm form,
            Authentication auth,
            Locale locale) {
        var notifications = schoolDriverEditService.sendAlert(auth.getName(), form);
        var views = notifications.stream().map(SchoolDriverNotificationView::new).toList();
        return new EntityApiResponse<>(
                Message.get("school.driver.alert.success", locale),
                views
        );
    }

    @Autowired
    public void setSchoolDriverReadService(SchoolDriverReadService schoolDriverReadService) {
        this.schoolDriverReadService = schoolDriverReadService;
    }

    @Autowired
    public void setSchoolDriverEditService(SchoolDriverEditService schoolDriverEditService) {
        this.schoolDriverEditService = schoolDriverEditService;
    }
}
