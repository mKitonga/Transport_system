package com.transport.parents.controller;

import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.Message;
import com.transport.liby.view.EntityApiResponse;
import com.transport.liby.view.PagedEntityApiResponse;
import com.transport.parents.form.MakeSchoolTripPaymentForm;
import com.transport.parents.service.ParentEditService;
import com.transport.parents.service.ParentReadService;
import com.transport.parents.view.ParentNotificationsView;
import com.transport.parents.view.ParentTripsView;
import com.transport.parents.view.ParentView;
import com.transport.school.View.SchoolTripPaymentView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static com.transport.liby.service.SystemConfig.STS_USER_BASE_URL;

@RestController
@RequestMapping(STS_USER_BASE_URL + "/parent")
public class ParentController {

    private ParentReadService parentReadService;
    private ParentEditService parentEditService;


    @PreAuthorize("hasAuthority('VIEW_CHILDREN')")
    @GetMapping("/child")
    public EntityApiResponse<ParentView> getChild(Authentication auth) {
        var profile = parentReadService.getChildProfile(auth.getName());
        return new EntityApiResponse<>(profile);
    }


    @PreAuthorize("hasAuthority('VIEW_SCHOOL_TRIPS')")
    @GetMapping("/trips")
    public EntityApiResponse<ParentTripsView> getTrips(Authentication auth) {
        var trips = parentReadService.getTrips(auth.getName());
        return new EntityApiResponse<>(trips);
    }


    @PreAuthorize("hasAuthority('VIEW_PAYMENTS')")
    @GetMapping("/payments")
    public PagedEntityApiResponse<SchoolTripPaymentView> listPayments(
            @RequestParam(name = "pageNum", required = false) Integer pageNum,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            Authentication auth) {
        var form = new BaseFetchForm();
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = parentReadService.listPayments(auth.getName(), form);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @PreAuthorize("hasAuthority('MAKE_PAYMENTS')")
    @PostMapping("/payments")
    public EntityApiResponse<SchoolTripPaymentView> makePayment(
            @Valid @RequestBody MakeSchoolTripPaymentForm form,
            Authentication auth,
            Locale locale) {
        var payment = parentEditService.makePayment(auth.getName(), form);
        return new EntityApiResponse<>(
                Message.get("school.trip.payment.initiated", locale),
                new SchoolTripPaymentView(payment)
        );
    }

    @PreAuthorize("hasAuthority('RECEIVE_NOTIFICATIONS')")
    @GetMapping("/notifications")
    public EntityApiResponse<ParentNotificationsView> getNotifications(Authentication auth) {
        var notifications = parentReadService.getNotifications(auth.getName());
        return new EntityApiResponse<>(notifications);
    }

    @Autowired
    public void setParentReadService(ParentReadService parentReadService) {
        this.parentReadService = parentReadService;
    }

    @Autowired
    public void setParentEditService(ParentEditService parentEditService) {
        this.parentEditService = parentEditService;
    }
}
