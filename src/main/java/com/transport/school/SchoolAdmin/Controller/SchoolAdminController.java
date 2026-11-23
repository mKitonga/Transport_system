package com.transport.school.SchoolAdmin.Controller;

import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.Message;
import com.transport.liby.view.EntityApiResponse;
import com.transport.liby.view.PagedEntityApiResponse;
import com.transport.parents.view.ParentView;
import com.transport.sacco.SaccoAdmin.View.SaccoAdminView;
import com.transport.sacco.SaccoDriver.View.SchoolTransportNotificationView;
import com.transport.sacco.View.SaccoMessageView;
import com.transport.sacco.View.SaccoTransportRequestView;
import com.transport.school.SchoolAdmin.Form.AssignDailyTripStudentsForm;
import com.transport.school.SchoolAdmin.Form.CreateSaccoTransportRequestForm;
import com.transport.school.SchoolAdmin.Form.SendSaccoMessageForm;
import com.transport.school.SchoolAdmin.Service.SchoolAdminEditService;
import com.transport.school.SchoolAdmin.Service.SchoolAdminReadService;
import com.transport.school.SchoolAdmin.View.SchoolAdminOverviewView;
import com.transport.school.SchoolAdmin.View.SchoolAdminView;
import com.transport.school.SchoolAdmin.View.SchoolDriverView;
import com.transport.school.SchoolDriver.View.SchoolDriverAssignmentView;
import com.transport.school.SchoolDriver.View.SchoolDriverNotificationView;
import com.transport.school.View.SchoolTripPaymentView;
import com.transport.user.entity.UserStatus;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;

import static com.transport.liby.service.SystemConfig.STS_USER_BASE_URL;

@RestController
@RequestMapping(STS_USER_BASE_URL + "/school-admin")
public class SchoolAdminController {

    private SchoolAdminReadService schoolAdminReadService;
    private SchoolAdminEditService schoolAdminEditService;



    @PreAuthorize("hasAuthority('VIEW_SCHOOL_ADMIN_OVERVIEW')")
    @GetMapping("/overview")
    public EntityApiResponse<SchoolAdminOverviewView> getOverview(Authentication auth) {
        var overview = schoolAdminReadService.getOverview(auth.getName());
        return new EntityApiResponse<>(overview);
    }

    @GetMapping("/profile")
    public EntityApiResponse<SchoolAdminView> getProfile(Authentication auth) {
        var admin = schoolAdminReadService.findActiveAdminByEntityId(auth.getName())
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));
        return new EntityApiResponse<>(new SchoolAdminView(admin));
    }

    @PreAuthorize("hasAuthority('VIEW_SCHOOL_DRIVERS')")
    @GetMapping("/school-drivers")
    public PagedEntityApiResponse<SchoolDriverView> listSchoolDrivers(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "status", required = false) UserStatus status,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {

        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = schoolAdminReadService.listSchoolDrivers(auth.getName(), query, status, form);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @PreAuthorize("hasAuthority('APPROVE_SCHOOL_DRIVER')")
    @PutMapping("/school-drivers/{driverId}/approve")
    public EntityApiResponse<SchoolDriverView> approveSchoolDriver(
            @PathVariable String driverId,
            Authentication auth,
            Locale locale) {
        var driver = schoolAdminEditService.approveSchoolDriver(driverId, auth.getName(), auth.getName());
        return new EntityApiResponse<>(
                Message.get("school.driver.approval.success", locale),
                new SchoolDriverView(driver)
        );
    }

    @PreAuthorize("hasAuthority('RECEIVE_NOTIFICATIONS')")
    @GetMapping("/notifications")
    public PagedEntityApiResponse<SchoolTransportNotificationView> listNotifications(
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {

        var form = new BaseFetchForm();
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = schoolAdminReadService.listNotifications(auth.getName(), form);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @PreAuthorize("hasAuthority('GET_DRIVER_NOTIFICATIONS')")
    @GetMapping("/driver-alerts")
    public PagedEntityApiResponse<SchoolDriverNotificationView> listDriverAlerts(
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {

        var form = new BaseFetchForm();
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = schoolAdminReadService.listDriverAlerts(auth.getName(), form);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @PreAuthorize("hasAuthority('ASSIGN_STUDENTS')")
    @PostMapping("assign-students/trips/daily/{tripId}")
    public EntityApiResponse<List<SchoolDriverAssignmentView>> assignDailyTripStudents(
            @PathVariable String tripId,
            @Valid @RequestBody AssignDailyTripStudentsForm form,
            Authentication auth,
            Locale locale) {
        var assignments = schoolAdminEditService.assignDailyTripStudents(auth.getName(), tripId, form, auth.getName());
        var views = assignments.stream().map(SchoolDriverAssignmentView::new).toList();
        return new EntityApiResponse<>(
                Message.get("school.daily.trip.assignment.success", locale),
                views
        );
    }

    @GetMapping("/parents")
    public PagedEntityApiResponse<ParentView> listParents(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {

        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = schoolAdminReadService.listParents(auth.getName(), query, form);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @GetMapping("/sacco-directory")
    public PagedEntityApiResponse<SaccoAdminView> listSaccoDirectory(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {

        var form = new BaseFetchForm();
        form.setQuery(query);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = schoolAdminReadService.listSaccoDirectory(query, form);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @PreAuthorize("hasAuthority('MAKE_SACCO_REQUESTS')")
    @GetMapping("/sacco-requests")
    public PagedEntityApiResponse<SaccoTransportRequestView> listTransportRequests(
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {

        var form = new BaseFetchForm();
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = schoolAdminReadService.listTransportRequests(auth.getName(), form);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @PreAuthorize("hasAuthority('MAKE_SACCO_REQUESTS')")
    @PostMapping("/sacco-requests")
    public EntityApiResponse<SaccoTransportRequestView> createTransportRequest(
            @Valid @RequestBody CreateSaccoTransportRequestForm form,
            Authentication auth,
            Locale locale) {
        var request = schoolAdminEditService.createTransportRequest(auth.getName(), form);
        return new EntityApiResponse<>(
                Message.get("school.transport.request.success", locale),
                new SaccoTransportRequestView(request)
        );
    }

    @PreAuthorize("hasAuthority('GET_SACCO_MESSAGES')")
    @PostMapping("/sacco-messages")
    public EntityApiResponse<SaccoMessageView> sendSaccoMessage(
            @Valid @RequestBody SendSaccoMessageForm form,
            Authentication auth,
            Locale locale) {
        var message = schoolAdminEditService.sendSaccoMessage(auth.getName(), form);
        return new EntityApiResponse<>(
                Message.get("school.sacco.message.success", locale),
                new SaccoMessageView(message)
        );
    }

    @PreAuthorize("hasAuthority('VIEW_PAYMENTS')")
    @GetMapping("/payments")
    public PagedEntityApiResponse<SchoolTripPaymentView> listPayments(
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {
        var form = new BaseFetchForm();
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = schoolAdminReadService.listPayments(auth.getName(), status, form);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @PreAuthorize("hasAuthority('APPROVE_PAYMENTS')")
    @PutMapping("approve/payments/{paymentId}")
    public EntityApiResponse<SchoolTripPaymentView> approvePayment(
            @PathVariable String paymentId,
            Authentication auth,
            Locale locale) {
        var payment = schoolAdminEditService.approvePayment(paymentId, auth.getName());
        return new EntityApiResponse<>(
                Message.get("school.payment.approval.success", locale),
                new SchoolTripPaymentView(payment)
        );
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
