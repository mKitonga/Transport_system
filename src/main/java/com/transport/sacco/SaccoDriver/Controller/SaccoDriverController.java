package com.transport.sacco.SaccoDriver.Controller;

import com.transport.fares.view.FareView;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.Message;
import com.transport.liby.view.EntityApiResponse;
import com.transport.liby.view.PagedEntityApiResponse;
import com.transport.routes.View.RouteView;
import com.transport.sacco.SaccoDriver.Form.CreateFarePromptForm;
import com.transport.sacco.SaccoDriver.Form.SchoolTransportUpdateForm;
import com.transport.sacco.SaccoDriver.Form.UpdateSaccoDriverVehicleForm;
import com.transport.sacco.SaccoDriver.Service.SaccoDriverEditService;
import com.transport.sacco.SaccoDriver.Service.SaccoDriverReadService;
import com.transport.sacco.SaccoDriver.View.PassengerPaymentView;
import com.transport.sacco.SaccoDriver.View.SaccoDriverOverviewView;
import com.transport.sacco.SaccoDriver.View.SaccoDriverView;
import com.transport.sacco.SaccoDriver.View.SchoolTransportNotificationView;
import com.transport.trips.view.TripView;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Locale;

import static com.transport.liby.service.SystemConfig.STS_USER_BASE_URL;

@RestController
@RequestMapping(STS_USER_BASE_URL + "/sacco-driver")
public class SaccoDriverController {

    private SaccoDriverReadService saccoDriverReadService;
    private SaccoDriverEditService saccoDriverEditService;

    @PreAuthorize("hasAuthority('SACCO_DRIVER')")
    @GetMapping("/overview")
    public EntityApiResponse<SaccoDriverOverviewView> getOverview(Authentication auth) {
        var overview = saccoDriverReadService.getOverview(auth.getName());
        return new EntityApiResponse<>(overview);
    }

    @GetMapping("/profile")
    public EntityApiResponse<SaccoDriverView> getProfile(Authentication auth) {
        var driver = saccoDriverReadService.findByEntityId(auth.getName());
        return new EntityApiResponse<>(new SaccoDriverView(driver));
    }

    @PreAuthorize("hasAuthority('SACCO_DRIVER')")
    @GetMapping("/assigned-route")
    public ResponseEntity<RouteView> getMyRoute(Authentication auth) {
        var routeView = saccoDriverReadService.getMyRoute(auth.getName());
        if (routeView == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(routeView);
    }

    @PreAuthorize("hasAuthority('SACCO_DRIVER')")
    @PutMapping("/update-vehicle")
    public EntityApiResponse<SaccoDriverView> updateVehicle(
            @RequestBody @Valid UpdateSaccoDriverVehicleForm form,
            Authentication auth,
            Locale locale) {
        var driver = saccoDriverEditService.updateVehicleDetails(
                auth.getName(),
                form.getNumberPlate(),
                form.getMatatuName()
        );
        return new EntityApiResponse<>(
                Message.get("driver.vehicle.update.success", locale),
                new SaccoDriverView(driver)
        );
    }

    @GetMapping("/view-fares")
    public PagedEntityApiResponse<FareView> listFares(
            @RequestParam(name = "routeId", required = false) String routeId,
            @RequestParam(name = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            Authentication auth) {
        var form = new BaseFetchForm();
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = saccoDriverReadService.listFaresForDriver(auth.getName(), routeId, form);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @PreAuthorize("hasAuthority('SACCO_DRIVER')")
    @PostMapping("/fare-prompts")
    public EntityApiResponse<PassengerPaymentView> createFarePrompt(
            @RequestBody @Valid CreateFarePromptForm form,
            Authentication auth,
            Locale locale) {
        var payment = saccoDriverEditService.createFarePrompt(auth.getName(), form);
        return new EntityApiResponse<>(
                Message.get("driver.fare.prompt.success", locale),
                new PassengerPaymentView(payment)
        );
    }

    @PreAuthorize("hasAuthority('SACCO_DRIVER')")
    @GetMapping("/dropoffs")
    public PagedEntityApiResponse<PassengerPaymentView> listDropoffs(
            @RequestParam(name = "onlyUncleared", required = false, defaultValue = "true") boolean onlyUncleared,
            @RequestParam(name = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "50") Integer pageSize,
            Authentication auth) {
        var form = new BaseFetchForm();
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = saccoDriverReadService.listDropoffs(auth.getName(), onlyUncleared, form);
        return new PagedEntityApiResponse<>(page, page.getContent());
    }

    @PreAuthorize("hasAuthority('SACCO_DRIVER')")
    @PutMapping("clear/dropoffs/{paymentId}")
    public EntityApiResponse<PassengerPaymentView> clearDropoff(
            @PathVariable String paymentId,
            Authentication auth,
            Locale locale) {
        var payment = saccoDriverEditService.clearDropoff(auth.getName(), paymentId);
        return new EntityApiResponse<>(
                Message.get("driver.dropoff.clear.success", locale),
                new PassengerPaymentView(payment)
        );
    }

    @PreAuthorize("hasAuthority('SACCO_DRIVER')")
    @GetMapping("/school-assignments")
    public PagedEntityApiResponse<TripView> listSchoolAssignments(
            @RequestParam(name = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            Authentication auth) {
        var form = new BaseFetchForm();
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = saccoDriverReadService.listSchoolAssignments(auth.getName(), form);
        var views = page.getContent().stream().map(TripView::toView).toList();
        return new PagedEntityApiResponse<>(page, views);
    }

    @PreAuthorize("hasAuthority('SACCO_DRIVER')")
    @PostMapping("updates/school-assignments/{tripId}")
    public EntityApiResponse<SchoolTransportNotificationView> sendSchoolUpdate(
            @PathVariable String tripId,
            @RequestBody @Valid SchoolTransportUpdateForm form,
            Authentication auth,
            Locale locale) {
        var notification = saccoDriverEditService.sendSchoolUpdate(auth.getName(), tripId, form);
        return new EntityApiResponse<>(
                Message.get("driver.school.update.success", locale),
                new SchoolTransportNotificationView(notification)
        );
    }

    @Autowired
    public void setSaccoDriverReadService(SaccoDriverReadService saccoDriverReadService) {
        this.saccoDriverReadService = saccoDriverReadService;
    }

    @Autowired
    public void setSaccoDriverEditService(SaccoDriverEditService saccoDriverEditService) {
        this.saccoDriverEditService = saccoDriverEditService;
    }
}
