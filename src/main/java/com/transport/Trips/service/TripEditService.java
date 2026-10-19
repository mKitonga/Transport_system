package com.transport.Trips.service;

import com.transport.Routes.Entity.Route;
import com.transport.Routes.Service.RouteReadService;
import com.transport.Sacco.Entity.Sacco;
import com.transport.Sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.Sacco.SaccoAdmin.Service.SaccoAdminReadService;
import com.transport.Sacco.Service.SaccoReadService;
import com.transport.School.Entity.School;
import com.transport.School.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.School.SchoolAdmin.Service.SchoolAdminReadService;
import com.transport.Trips.entity.ClosingDayTrip;
import com.transport.Trips.entity.DailyPickupTrip;
import com.transport.Trips.entity.EducationalTrip;
import com.transport.Trips.entity.Trip;
import com.transport.Trips.entity.TripType;
import com.transport.Trips.form.EditTripForm;
import com.transport.Trips.form.RegisterClosingDayTripForm;
import com.transport.Trips.form.RegisterDailyPickupTripForm;
import com.transport.Trips.form.RegisterEducationalTripForm;
import com.transport.Trips.repository.TripRepository;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.form.SessionUserIdForm;
import com.transport.liby.service.BaseJpaRepoEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TripEditService extends BaseJpaRepoEditService<Trip, TripRepository> {

    private SchoolAdminReadService schoolAdminReadService;
    private RouteReadService routeReadService;
    private SaccoAdminReadService saccoAdminReadService;
    private SaccoReadService saccoReadService;

    @Transactional
    public DailyPickupTrip registerDailyPickupTrip(RegisterDailyPickupTripForm form) {
        var schoolAdmin = getSchoolAdmin(form.getSessionUserId());

        var trip = new DailyPickupTrip();

        applyCommonFields(trip, form, schoolAdmin, TripType.DAILY_PICKUP);

        trip.setRoute(findSchoolRoute(form.getRouteId(), schoolAdmin.getSchool()));
        trip.setPickupTime(form.getPickupTime());
        trip.setDropoffTime(form.getDropoffTime());
        trip.setCreatedByEntityId(form.getSessionUserId());
        trip = (DailyPickupTrip) save(trip, form.getSessionUserId());
        return trip;
    }

    @Transactional
    public EducationalTrip registerEducationalTrip(RegisterEducationalTripForm form) {
        var schoolAdmin = getSchoolAdmin(form.getSessionUserId());

        var trip = new EducationalTrip();

        applyCommonFields(trip, form, schoolAdmin, TripType.EDUCATIONAL_TRIP);

        trip.setTripTitle(form.getTripTitle().trim());
        trip.setDestination(form.getDestination().trim());
        trip.setDurationDays(form.getDurationDays());
        trip.setTripDate(form.getTripDate());
        setSaccoFields(trip, form.getSaccoAdminId(), form.getSaccoId());
        trip.setCreatedByEntityId(form.getSessionUserId());

        trip = (EducationalTrip) save(trip, form.getSessionUserId());
        return trip;
    }

    @Transactional
    public ClosingDayTrip registerClosingDayTrip(RegisterClosingDayTripForm form) {
        var schoolAdmin = getSchoolAdmin(form.getSessionUserId());

        var trip = new ClosingDayTrip();

        applyCommonFields(trip, form, schoolAdmin, TripType.CLOSING_DAY_TRIP);

        var saccoAdmin = findSaccoAdmin(form.getSaccoAdminId());
        var sacco = resolveSacco(saccoAdmin, form.getSaccoId());
        trip.setSaccoAdmin(saccoAdmin);
        trip.setSaccoName(sacco);
        trip.setRoute(findClosingRoute(form.getRouteId(), schoolAdmin.getSchool(), sacco));
        trip.setTripDate(form.getTripDate());
        trip.setCreatedByEntityId(form.getSessionUserId());

        trip = (ClosingDayTrip) save(trip, form.getSessionUserId());
        return trip;
    }

    @Transactional
    public DailyPickupTrip editDailyPickupTrip(String tripId, RegisterDailyPickupTripForm form) {
        var schoolAdmin = getSchoolAdmin(form.getSessionUserId());

        var trip = findOwnedTrip(tripId, schoolAdmin, DailyPickupTrip.class);

        applyCommonFields(trip, form, schoolAdmin, TripType.DAILY_PICKUP);

        trip.setRoute(findSchoolRoute(form.getRouteId(), schoolAdmin.getSchool()));
        trip.setPickupTime(form.getPickupTime());
        trip.setDropoffTime(form.getDropoffTime());
        trip.setCreatedByEntityId(form.getSessionUserId());

        trip = (DailyPickupTrip) save(trip, form.getSessionUserId());
        return trip;
    }

    @Transactional
    public EducationalTrip editEducationalTrip(String tripId, RegisterEducationalTripForm form) {
        var schoolAdmin = getSchoolAdmin(form.getSessionUserId());

        var trip = findOwnedTrip(tripId, schoolAdmin, EducationalTrip.class);

        applyCommonFields(trip, form, schoolAdmin, TripType.EDUCATIONAL_TRIP);

        trip.setTripTitle(form.getTripTitle().trim());
        trip.setDestination(form.getDestination().trim());
        trip.setDurationDays(form.getDurationDays());
        trip.setTripDate(form.getTripDate());
        setSaccoFields(trip, form.getSaccoAdminId(), form.getSaccoId());
        trip.setCreatedByEntityId(form.getSessionUserId());

        trip = (EducationalTrip) save(trip, form.getSessionUserId());
        return trip;
    }

    @Transactional
    public ClosingDayTrip editClosingDayTrip(String tripId, RegisterClosingDayTripForm form) {
        var schoolAdmin = getSchoolAdmin(form.getSessionUserId());

        var trip = findOwnedTrip(tripId, schoolAdmin, ClosingDayTrip.class);

        applyCommonFields(trip, form, schoolAdmin, TripType.CLOSING_DAY_TRIP);

        var saccoAdmin = findSaccoAdmin(form.getSaccoAdminId());
        var sacco = resolveSacco(saccoAdmin, form.getSaccoId());
        trip.setSaccoAdmin(saccoAdmin);
        trip.setSaccoName(sacco);
        trip.setRoute(findClosingRoute(form.getRouteId(), schoolAdmin.getSchool(), sacco));
        trip.setTripDate(form.getTripDate());
        trip.setCreatedByEntityId(form.getSessionUserId());

        trip = (ClosingDayTrip) save(trip, form.getSessionUserId());
        return trip;
    }

    @Transactional
    public void deleteTrip(String tripId, SessionUserIdForm form) {
        var schoolAdmin = getSchoolAdmin(form.getSessionUserId());
        var trip = findOwnedTrip(tripId, schoolAdmin, Trip.class);
        delete(trip, form.getSessionUserId());
    }

    private void applyCommonFields(Trip trip, EditTripForm form, SchoolAdmin schoolAdmin,
            TripType expectedType) {
        if (form.getTripType() != expectedType) {
            throw badRequest("error.trip.type.mismatch");
        }
        var school = schoolAdmin.getSchool();
        if (school == null) {
            throw notFound();
        }
        trip.setSchoolAdmin(schoolAdmin);
        trip.setSchoolName(school.getName());
        trip.setLocation(school.getLocation());
        trip.setTripType(expectedType);
        trip.setGrade(form.getGrade());
        trip.setAmount(form.getAmount());
    }

    private SchoolAdmin getSchoolAdmin(String adminId) {
        if (adminId == null || adminId.isBlank()) {
            throw notFound();
        }
        return schoolAdminReadService.findActiveAdminByEntityId(adminId)
                .orElseThrow(this::notFound);
    }

    private Route findSchoolRoute(String routeId, School school) {
        var route = routeReadService.findByEntityId(routeId);
        if (route.getSchool() == null
                || route.getDeletedByEntityId() != null
                || !route.getSchool().getEntityId().equals(school.getEntityId())) {
            throw notFound();
        }
        return route;
    }

    private Route findClosingRoute(String routeId, School school, Sacco sacco) {
        var route = routeReadService.findByEntityId(routeId);
        if (route.getDeletedByEntityId() != null) {
            throw notFound();
        }
        if (route.getSacco() != null) {
            if (sacco == null || !route.getSacco().getEntityId().equals(sacco.getEntityId())) {
                throw notFound();
            }
        } else if (route.getSchool() == null || !route.getSchool().getEntityId().equals(school.getEntityId())) {
            throw notFound();
        }
        return route;
    }

    private SaccoAdmin findSaccoAdmin(String saccoAdminId) {
        if (saccoAdminId == null || saccoAdminId.isBlank()) {
            throw notFound();
        }
        return saccoAdminReadService.findActiveAdminByEntityId(saccoAdminId)
                .orElseThrow(this::notFound);
    }

    private SaccoAdmin findOptionalSaccoAdmin(String saccoAdminId) {
        if (saccoAdminId == null || saccoAdminId.isBlank()) {
            return null;
        }
        return findSaccoAdmin(saccoAdminId);
    }

    private void setSaccoFields(EducationalTrip trip, String saccoAdminId, String saccoId) {
        var saccoAdmin = findOptionalSaccoAdmin(saccoAdminId);
        trip.setSaccoAdmin(saccoAdmin);
        trip.setSacco(resolveSacco(saccoAdmin, saccoId));
    }

    private Sacco resolveSacco(SaccoAdmin saccoAdmin, String saccoId) {
        Sacco requestedSacco = saccoId == null || saccoId.isBlank()
                ? null
                : saccoReadService.findByEntityId(saccoId);
        Sacco adminSacco = saccoAdmin == null ? null : saccoAdmin.getSacco();
        if (requestedSacco != null && adminSacco != null
                && !requestedSacco.getEntityId().equals(adminSacco.getEntityId())) {
            throw notFound();
        }
        return requestedSacco != null ? requestedSacco : adminSacco;
    }

    private <T extends Trip> T findOwnedTrip(String tripId, SchoolAdmin schoolAdmin, Class<T> tripClass) {
        Trip trip = findByEntityId(tripId);
        School school = schoolAdmin.getSchool();
        if (!tripClass.isInstance(trip)
                || school == null
                || trip.getSchoolAdmin() == null
                || trip.getSchoolAdmin().getSchool() == null
                || !trip.getSchoolAdmin().getSchool().getEntityId().equals(school.getEntityId())) {
            throw notFound();
        }
        return tripClass.cast(trip);
    }

    private CommonRuntimeException notFound() {
        return new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
    }

    private CommonRuntimeException badRequest(String message) {
        return new CommonRuntimeException(ExceptionType.BAD_REQUEST, message);
    }

    @Autowired
    public void setSchoolAdminReadService(SchoolAdminReadService service) {
        schoolAdminReadService = service;
    }

    @Autowired
    public void setRouteReadService(RouteReadService service) {
        routeReadService = service;
    }

    @Autowired
    public void setSaccoAdminReadService(SaccoAdminReadService service) {
        saccoAdminReadService = service;
    }

    @Autowired
    public void setSaccoReadService(SaccoReadService service) {
        saccoReadService = service;
    }
}
