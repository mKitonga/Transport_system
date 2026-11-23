package com.transport.sacco.SaccoDriver.Service;

import com.transport.fares.service.FareReadService;
import com.transport.fares.view.FareView;
import com.transport.liby.form.BaseFetchForm;
import com.transport.queue.Service.QueueEntryReadService;
import com.transport.queue.View.QueueEntryView;
import com.transport.routes.View.RouteView;
import com.transport.sacco.SaccoDriver.Entity.PassengerPayment;
import com.transport.sacco.SaccoDriver.Entity.SaccoDriver;
import com.transport.sacco.SaccoDriver.Repository.PassengerPaymentRepository;
import com.transport.sacco.SaccoDriver.Repository.SaccoDriverRepository;
import com.transport.sacco.SaccoDriver.View.PassengerPaymentView;
import com.transport.sacco.SaccoDriver.View.SaccoDriverOverviewView;
import com.transport.sacco.SaccoDriver.View.SaccoDriverView;
import com.transport.trips.entity.Trip;
import com.transport.trips.repository.TripRepository;
import com.transport.user.entity.UserStatus;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoReadService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaccoDriverReadService extends BaseJpaRepoReadService<SaccoDriver, SaccoDriverRepository> {

    private QueueEntryReadService queueEntryReadService;
    private PassengerPaymentRepository passengerPaymentRepository;
    private TripRepository tripRepository;
    private FareReadService fareReadService;

    @Transactional(readOnly = true)
    public Page<SaccoDriverView> listDrivers(BaseFetchForm form, String saccoId, UserStatus status) {
        Specification<SaccoDriver> spec = repository.notDeleted()
                .and(repository.saccoEntityIdIs(saccoId));

        if (status != null) {
            spec = spec.and(repository.userStatusIs(status));
        }

        if (StringUtils.isNotBlank(form.getQuery())) {
            spec = spec.and(
                    repository.nameLike(form.getQuery())
                            .or(repository.phoneNumberLike(form.getQuery()))
                            .or(repository.emailLike(form.getQuery()))
            );
        }

        return repository.findAll(spec, repository.defaultPageable(form))
                .map(SaccoDriverView::new);
    }

    public SaccoDriver findActiveByEntityIdForUpdate(String driverId) {
        return repository.findActiveByEntityIdForUpdate(driverId)
                .orElseThrow(() -> new CommonRuntimeException(
                        ExceptionType.NOT_FOUND,
                        "error.entity.not.found"
                ));
    }

    @Transactional(readOnly = true)
    public SaccoDriverOverviewView getOverview(String driverId) {
        var driver = findByEntityId(driverId);

        var currentQueue = queueEntryReadService.findCurrentEntry(driverId)
                .map(QueueEntryView::new)
                .orElse(null);

        var routeView = driver.getAssignedRoute() != null
                ? new RouteView(driver.getAssignedRoute())
                : null;

        long farePromptsCount = passengerPaymentRepository.count(
                passengerPaymentRepository.notDeleted()
                        .and(passengerPaymentRepository.driverEntityIdIs(driverId))
        );

        long pendingDropoffsCount = passengerPaymentRepository.count(
                passengerPaymentRepository.notDeleted()
                        .and(passengerPaymentRepository.driverEntityIdIs(driverId))
                        .and(passengerPaymentRepository.unclearedOnly())
        );

        long schoolAssignmentsCount = tripRepository.count(
                tripRepository.notDeleted()
                        .and(tripRepository.assignedDriverEntityIdIs(driverId))
        );

        return SaccoDriverOverviewView.builder()
                .driverName(driver.getName())
                .saccoName(driver.getSacco() != null ? driver.getSacco().getName() : null)
                .numberPlate(driver.getNumberPlate())
                .matatuName(driver.getMatatuName())
                .status(driver.getUserStatus())
                .assignedRoute(routeView)
                .currentQueueEntry(currentQueue)
                .totalFarePrompts(farePromptsCount)
                .pendingDropoffs(pendingDropoffsCount)
                .schoolAssignmentsCount(schoolAssignmentsCount)
                .build();
    }

    @Transactional(readOnly = true)
    public RouteView getMyRoute(String driverId) {
        SaccoDriver driver = findByEntityId(driverId);
        if (driver.getAssignedRoute() == null) {
            return null;
        }
        return new RouteView(driver.getAssignedRoute());
    }

    @Transactional(readOnly = true)
    public Page<FareView> listFaresForDriver(String driverId, String routeId, BaseFetchForm form) {
        SaccoDriver driver = findByEntityId(driverId);
        if (driver.getSacco() == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        String resolvedRouteId = routeId;
        if (StringUtils.isBlank(resolvedRouteId) && driver.getAssignedRoute() != null) {
            resolvedRouteId = driver.getAssignedRoute().getEntityId();
        }

        return fareReadService.listFaresForSaccoOrRoute(driver.getSacco().getEntityId(), resolvedRouteId, form);
    }

    @Transactional(readOnly = true)
    public Page<PassengerPaymentView> listDropoffs(String driverId, boolean onlyUncleared, BaseFetchForm form) {
        Specification<PassengerPayment> spec = passengerPaymentRepository.notDeleted()
                .and(passengerPaymentRepository.driverEntityIdIs(driverId));

        if (onlyUncleared) {
            spec = spec.and(passengerPaymentRepository.unclearedOnly());
        }

        return passengerPaymentRepository.findAll(spec, passengerPaymentRepository.defaultPageable(form))
                .map(PassengerPaymentView::new);
    }

    @Transactional(readOnly = true)
    public Page<Trip> listSchoolAssignments(String driverId, BaseFetchForm form) {
        Specification<Trip> spec = tripRepository.notDeleted()
                .and(tripRepository.assignedDriverEntityIdIs(driverId));

        return tripRepository.findAll(spec, tripRepository.defaultPageable(form));
    }

    @Autowired
    public void setQueueEntryReadService(QueueEntryReadService queueEntryReadService) {
        this.queueEntryReadService = queueEntryReadService;
    }

    @Autowired
    public void setPassengerPaymentRepository(PassengerPaymentRepository passengerPaymentRepository) {
        this.passengerPaymentRepository = passengerPaymentRepository;
    }

    @Autowired
    public void setTripRepository(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Autowired
    public void setFareReadService(FareReadService fareReadService) {
        this.fareReadService = fareReadService;
    }
}
