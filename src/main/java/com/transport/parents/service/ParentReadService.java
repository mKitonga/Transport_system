package com.transport.parents.service;

import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.BaseJpaRepoReadService;
import com.transport.parents.entity.Parent;
import com.transport.parents.repository.ParentRepository;
import com.transport.parents.view.ParentNotificationsView;
import com.transport.parents.view.ParentTripsView;
import com.transport.parents.view.ParentView;
import com.transport.school.Entity.SchoolTripPayment;
import com.transport.school.Repository.SchoolTripPaymentRepository;
import com.transport.school.SchoolDriver.Entity.SchoolDriverAssignment;
import com.transport.school.SchoolDriver.Repository.SchoolDriverAssignmentRepository;
import com.transport.school.View.SchoolTripPaymentView;
import com.transport.trips.entity.ClosingDayTrip;
import com.transport.trips.entity.DailyPickupTrip;
import com.transport.trips.entity.EducationalTrip;
import com.transport.trips.entity.Trip;
import com.transport.trips.entity.TripType;
import com.transport.trips.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ParentReadService extends BaseJpaRepoReadService<Parent, ParentRepository> {

    private TripRepository tripRepository;
    private SchoolDriverAssignmentRepository assignmentRepository;
    private SchoolTripPaymentRepository paymentRepository;

    public Optional<Parent> findActiveParentByEntityId(String entityId) {
        return repository.findByEntityIdAndDeletedAtIsNull(entityId);
    }

    @Transactional(readOnly = true)
    public ParentView getChildProfile(String parentId) {
        var parent = findActiveParentByEntityId(parentId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));
        return new ParentView(parent);
    }

    @Transactional(readOnly = true)
    public ParentTripsView getTrips(String parentId) {
        var parent = findActiveParentByEntityId(parentId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        if (parent.getSchool() == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        String schoolId = parent.getSchool().getEntityId();

        var dailySpec = tripRepository.notDeleted()
                .and(tripRepository.tripTypeIs(TripType.DAILY_PICKUP))
                .and(tripRepository.schoolEntityIdIs(schoolId));
        if (parent.getGrade() != null) {
            dailySpec = dailySpec.and(tripRepository.schoolGradeIs(parent.getGrade()));
        }

        List<Trip> dailyTrips = tripRepository.findAll(dailySpec);
        List<ParentTripsView.DailyTripItem> dailyTripItems = new ArrayList<>();
        for (Trip t : dailyTrips) {
            if (t instanceof DailyPickupTrip daily) {
                var paymentOpt = paymentRepository.findByParent_EntityIdAndTripTypeAndTripId(parentId, "DAILY", daily.getEntityId());
                var assignmentOpt = assignmentRepository.findByDailyPickupTrip_EntityIdAndParent_EntityId(daily.getEntityId(), parentId);

                dailyTripItems.add(ParentTripsView.DailyTripItem.builder()
                        .tripId(daily.getEntityId())
                        .routeName(daily.getRoute() != null ? daily.getRoute().getName() : null)
                        .pickupTime(daily.getPickupTime())
                        .dropoffTime(daily.getDropoffTime())
                        .amount(daily.getAmount())
                        .paymentStatus(paymentOpt.map(SchoolTripPayment::getStatus).orElse("not_paid"))
                        .assignedPickupTime(assignmentOpt.map(SchoolDriverAssignment::getAssignedPickupTime).orElse(null))
                        .driverName(assignmentOpt.map(a -> a.getSchoolDriver() != null ? a.getSchoolDriver().getName() : null).orElse(null))
                        .driverPhone(assignmentOpt.map(a -> a.getSchoolDriver() != null ? a.getSchoolDriver().getPhoneNumber() : null).orElse(null))
                        .build());
            }
        }

        var eduSpec = tripRepository.notDeleted()
                .and(tripRepository.tripTypeIs(TripType.EDUCATIONAL_TRIP))
                .and(tripRepository.schoolEntityIdIs(schoolId));
        if (parent.getGrade() != null) {
            eduSpec = eduSpec.and(tripRepository.schoolGradeIs(parent.getGrade()));
        }

        List<Trip> eduTrips = tripRepository.findAll(eduSpec);
        List<ParentTripsView.EducationalTripItem> eduTripItems = new ArrayList<>();
        for (Trip t : eduTrips) {
            if (t instanceof EducationalTrip edu) {
                var paymentOpt = paymentRepository.findByParent_EntityIdAndTripTypeAndTripId(parentId, "EDUCATIONAL", edu.getEntityId());

                eduTripItems.add(ParentTripsView.EducationalTripItem.builder()
                        .tripId(edu.getEntityId())
                        .tripTitle(edu.getTripTitle())
                        .destination(edu.getDestination())
                        .durationDays(edu.getDurationDays())
                        .tripDate(edu.getTripDate())
                        .amount(edu.getAmount())
                        .paymentStatus(paymentOpt.map(SchoolTripPayment::getStatus).orElse("not_paid"))
                        .build());
            }
        }

        var closingSpec = tripRepository.notDeleted()
                .and(tripRepository.tripTypeIs(TripType.CLOSING_DAY_TRIP))
                .and(tripRepository.schoolEntityIdIs(schoolId));

        List<Trip> closingTrips = tripRepository.findAll(closingSpec);
        List<ParentTripsView.ClosingTripItem> closingTripItems = new ArrayList<>();
        for (Trip t : closingTrips) {
            if (t instanceof ClosingDayTrip closing) {
                var paymentOpt = paymentRepository.findByParent_EntityIdAndTripTypeAndTripId(parentId, "CLOSING", closing.getEntityId());

                closingTripItems.add(ParentTripsView.ClosingTripItem.builder()
                        .tripId(closing.getEntityId())
                        .routeName(closing.getRoute() != null ? closing.getRoute().getName() : null)
                        .tripDate(closing.getTripDate())
                        .amount(closing.getAmount())
                        .paymentStatus(paymentOpt.map(SchoolTripPayment::getStatus).orElse("not_paid"))
                        .driverName(closing.getAssignedDriver() != null ? closing.getAssignedDriver().getName() : null)
                        .driverPhone(closing.getAssignedDriver() != null ? closing.getAssignedDriver().getPhoneNumber() : null)
                        .numberPlate(closing.getAssignedDriver() != null ? closing.getAssignedDriver().getNumberPlate() : null)
                        .build());
            }
        }

        return ParentTripsView.builder()
                .child(new ParentView(parent))
                .dailyTrips(dailyTripItems)
                .educationalTrips(eduTripItems)
                .closingTrips(closingTripItems)
                .build();
    }

    @Transactional(readOnly = true)
    public Page<SchoolTripPaymentView> listPayments(String parentId, BaseFetchForm form) {
        var spec = paymentRepository.notDeleted()
                .and(paymentRepository.parentEntityIdIs(parentId));

        return paymentRepository.findAll(spec, paymentRepository.defaultPageable(form))
                .map(SchoolTripPaymentView::new);
    }

    @Transactional(readOnly = true)
    public ParentNotificationsView getNotifications(String parentId) {
        ParentTripsView trips = getTrips(parentId);

        List<ParentTripsView.DailyTripItem> assignedDaily = trips.getDailyTrips().stream()
                .filter(d -> d.getAssignedPickupTime() != null || d.getDriverName() != null)
                .toList();

        List<ParentTripsView.ClosingTripItem> assignedClosing = trips.getClosingTrips().stream()
                .filter(c -> c.getDriverName() != null)
                .toList();

        List<SchoolTripPaymentView> recentPayments = paymentRepository.findAllByParent_EntityId(parentId).stream()
                .map(SchoolTripPaymentView::new)
                .toList();

        return ParentNotificationsView.builder()
                .dailyAssignments(assignedDaily)
                .closingAssignments(assignedClosing)
                .payments(recentPayments)
                .build();
    }

    @Autowired
    public void setTripRepository(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Autowired
    public void setAssignmentRepository(SchoolDriverAssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Autowired
    public void setPaymentRepository(SchoolTripPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
