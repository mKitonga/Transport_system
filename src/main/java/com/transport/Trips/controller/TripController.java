package com.transport.Trips.controller;

import com.transport.School.Entity.SchoolGrade;
import com.transport.Trips.entity.ClosingDayTrip;
import com.transport.Trips.entity.DailyPickupTrip;
import com.transport.Trips.entity.EducationalTrip;
import com.transport.Trips.entity.Trip;
import com.transport.Trips.entity.TripStatus;
import com.transport.Trips.entity.TripType;
import com.transport.Trips.form.FetchTripForm;
import com.transport.Trips.form.RegisterClosingDayTripForm;
import com.transport.Trips.form.RegisterDailyPickupTripForm;
import com.transport.Trips.form.RegisterEducationalTripForm;
import com.transport.Trips.service.TripEditService;
import com.transport.Trips.service.TripReadService;
import com.transport.Trips.view.ClosingDayTripView;
import com.transport.Trips.view.DailyPickupTripView;
import com.transport.Trips.view.EducationalTripView;
import com.transport.Trips.view.TripView;
import com.transport.liby.form.SessionUserIdForm;
import com.transport.liby.service.Message;
import com.transport.liby.view.ApiResponse;
import com.transport.liby.view.EntityApiResponse;
import com.transport.liby.view.PagedEntityApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

import static com.transport.liby.service.SystemConfig.STS_USER_BASE_URL;

@RestController
@RequestMapping(STS_USER_BASE_URL + "/trips")
public class TripController {
    private TripEditService tripEditService;
    private TripReadService tripReadService;

    @PreAuthorize("hasAuthority('REGISTER_TRIP')")
    @PostMapping("/register/daily-pickup-trip")
    public EntityApiResponse<DailyPickupTripView> registerDailyPickupTrip(
            @RequestBody @Valid RegisterDailyPickupTripForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var trip = tripEditService.registerDailyPickupTrip(form);
        return new EntityApiResponse<>(
                Message.get("trip.registration.success", locale),
                new DailyPickupTripView(trip)
        );
    }

    @PreAuthorize("hasAuthority('REGISTER_TRIP')")
    @PostMapping("/register/educational-trip")
    public EntityApiResponse<EducationalTripView> registerEducationalTrip(
            @RequestBody @Valid RegisterEducationalTripForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var trip = tripEditService.registerEducationalTrip(form);
        return new EntityApiResponse<>(
                Message.get("trip.registration.success", locale),
                new EducationalTripView(trip)
        );
    }

    @PreAuthorize("hasAuthority('REGISTER_TRIP')")
    @PostMapping("/register/closing-day-trip")
    public EntityApiResponse<ClosingDayTripView> registerClosingDayTrip(
            @RequestBody @Valid RegisterClosingDayTripForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var trip = tripEditService.registerClosingDayTrip(form);
        return new EntityApiResponse<>(
                Message.get("trip.registration.success", locale),
                new ClosingDayTripView(trip)
        );
    }

    @PreAuthorize("hasAuthority('EDIT_TRIP')")
    @PutMapping("/edit/{tripId}/daily-pickup-trip")
    public EntityApiResponse<DailyPickupTripView> editDailyPickupTrip(
            @PathVariable String tripId,
            @RequestBody @Valid RegisterDailyPickupTripForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var trip = tripEditService.editDailyPickupTrip(tripId, form);
        return new EntityApiResponse<>(
                Message.get("trip.edit.success", locale),
                new DailyPickupTripView(trip));
    }

    @PreAuthorize("hasAuthority('EDIT_TRIP')")
    @PutMapping("/edit/{tripId}/educational-trip")
    public EntityApiResponse<EducationalTripView> editEducationalTrip(
            @PathVariable String tripId,
            @RequestBody @Valid RegisterEducationalTripForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var trip = tripEditService.editEducationalTrip(tripId, form);
        return new EntityApiResponse<>(
                Message.get("trip.edit.success", locale),
                new EducationalTripView(trip));
    }

    @PreAuthorize("hasAuthority('EDIT_TRIP')")
    @PutMapping("/edit/{tripId}/closing-day-trip")
    public EntityApiResponse<ClosingDayTripView> editClosingDayTrip(
            @PathVariable String tripId,
            @RequestBody @Valid RegisterClosingDayTripForm form,
            Authentication auth,
            Locale locale) {
        form.setSessionUserId(auth.getName());
        var trip = tripEditService.editClosingDayTrip(tripId, form);
        return new EntityApiResponse<>(
                Message.get("trip.edit.success", locale),
                new ClosingDayTripView(trip));
    }

    @PreAuthorize("hasAuthority('DELETE_TRIP')")
    @DeleteMapping("/delete/{tripId}")
    public ApiResponse deleteTrip(
            @PathVariable String tripId,
            Authentication auth,
            Locale locale) {
        var form = new SessionUserIdForm();
        form.setSessionUserId(auth.getName());
        tripEditService.deleteTrip(tripId, form);
        return new ApiResponse(Message.get("trip.delete.success", locale));
    }

    @GetMapping("/list")
    public PagedEntityApiResponse<TripView> listTrips(
            @RequestParam(name = "query", required = false) String query,
            @RequestParam(name = "schoolGrade", required = false) SchoolGrade schoolGrade,
            @RequestParam(name = "tripType", required = false) TripType tripType,
            @RequestParam(name = "tripStatus", required = false) TripStatus tripStatus,
            @RequestParam(name = "pageNum", required = false, defaultValue = "0") Integer pageNum,
            @RequestParam(name = "pageSize", required = false, defaultValue = "100") Integer pageSize,
            Authentication auth) {
        var form = new FetchTripForm();
        form.setQuery(query);
        form.setSchoolGrade(schoolGrade);
        form.setTripType(tripType);
        form.setTripStatus(tripStatus);
        form.setPageNum(pageNum);
        form.setPageSize(pageSize);
        form.setSessionUserId(auth.getName());

        var page = tripReadService.listTrips(form);
        var views = page.getContent().stream().map(this::toTripView).toList();
        return new PagedEntityApiResponse<>(page,views);
    }

    private TripView toTripView(Trip trip) {
        if (trip instanceof DailyPickupTrip dailyPickupTrip) {
            return new DailyPickupTripView(dailyPickupTrip);
        }
        if (trip instanceof EducationalTrip educationalTrip) {
            return new EducationalTripView(educationalTrip);
        }
        if (trip instanceof ClosingDayTrip closingDayTrip) {
            return new ClosingDayTripView(closingDayTrip);
        }
        throw new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unsupported trip type"
        );
    }

    @Autowired
    public void setTripEditService(TripEditService tripEditService) {
        this.tripEditService = tripEditService;
    }

    @Autowired
    public void setTripReadService(TripReadService tripReadService) {
        this.tripReadService = tripReadService;
    }
}
