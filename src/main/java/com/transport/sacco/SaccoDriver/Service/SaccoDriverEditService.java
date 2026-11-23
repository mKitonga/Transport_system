package com.transport.sacco.SaccoDriver.Service;

import com.transport.fares.entity.DayType;
import com.transport.fares.entity.Fare;
import com.transport.fares.entity.TimePeriod;
import com.transport.fares.service.FareReadService;
import com.transport.notification.sms.SmsService;
import com.transport.routes.Entity.Route;
import com.transport.routes.Entity.Terminal;
import com.transport.routes.Service.RouteReadService;
import com.transport.routes.Service.TerminalReadService;
import com.transport.sacco.SaccoDriver.Entity.PassengerPayment;
import com.transport.sacco.SaccoDriver.Entity.SaccoDriver;
import com.transport.sacco.SaccoDriver.Entity.SchoolTransportNotification;
import com.transport.sacco.SaccoDriver.Form.CreateFarePromptForm;
import com.transport.sacco.SaccoDriver.Form.SchoolTransportUpdateForm;
import com.transport.sacco.SaccoDriver.Repository.PassengerPaymentRepository;
import com.transport.sacco.SaccoDriver.Repository.SaccoDriverRepository;
import com.transport.sacco.SaccoDriver.Repository.SchoolTransportNotificationRepository;
import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.trips.entity.Trip;
import com.transport.trips.service.TripReadService;
import com.transport.user.entity.UserStatus;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoEditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Service
public class SaccoDriverEditService extends BaseJpaRepoEditService<SaccoDriver, SaccoDriverRepository> {

    private PassengerPaymentRepository passengerPaymentRepository;
    private SchoolTransportNotificationRepository schoolTransportNotificationRepository;
    private RouteReadService routeReadService;
    private TerminalReadService terminalReadService;
    private FareReadService fareReadService;
    private TripReadService tripReadService;
    private SmsService smsService;

    @Transactional
    public SaccoDriver assignRoute(String driverId, Route route, String updatedById) {
        var driver = findByEntityId(driverId);
        if (driver.getSacco() == null || route.getSacco() == null || !driver.getSacco().getEntityId().equals(route.getSacco().getEntityId())) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        driver.setAssignedRoute(route);
        return save(driver, updatedById);
    }

    @Transactional
    public void clearAssignedRoute(String driverId, String routeId, String updatedById) {
        var driver = findByEntityId(driverId);
        if (driver.getAssignedRoute() != null
                && driver.getAssignedRoute().getEntityId().equals(routeId)) {
            driver.setAssignedRoute(null);
            save(driver, updatedById);
        }
    }

    @Transactional
    public SaccoDriver approveDriver(String driverId, String saccoId, String updatedById) {
        var driver = findByEntityId(driverId);
        if (driver.getSacco() == null || !driver.getSacco().getEntityId().equals(saccoId)) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }
        driver.setUserStatus(UserStatus.ACTIVE);
        return save(driver, updatedById);
    }

    @Transactional
    public SaccoDriver suspendDriver(String driverId, String saccoId, String updatedById) {
        var driver = findByEntityId(driverId);
        if (driver.getSacco() == null || !driver.getSacco().getEntityId().equals(saccoId)) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }
        driver.setUserStatus(UserStatus.SUSPENDED);
        return save(driver, updatedById);
    }

    @Transactional
    public SaccoDriver updateVehicleDetails(String driverId, String numberPlate, String matatuName) {
        var driver = findByEntityId(driverId);
        if (numberPlate != null && !numberPlate.isBlank()) {
            driver.setNumberPlate(numberPlate.trim());
        }
        if (matatuName != null) {
            driver.setMatatuName(matatuName.trim());
        }
        return save(driver, driverId);
    }

    @Transactional
    public PassengerPayment createFarePrompt(String driverId, CreateFarePromptForm form) {
        var driver = findByEntityId(driverId);
        if (driver.getSacco() == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        var route = routeReadService.findByEntityId(form.getRouteId());
        if (route.getSacco() == null || !driver.getSacco().getEntityId().equals(route.getSacco().getEntityId())) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        var boardingTerminal = terminalReadService.findByEntityId(form.getBoardingTerminalId());
        var dropoffTerminal = terminalReadService.findByEntityId(form.getDropoffTerminalId());

        if (boardingTerminal.getEntityId().equals(dropoffTerminal.getEntityId())) {
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.terminals.must.be.different");
        }

        LocalDateTime now = LocalDateTime.now();
        var dayOfWeek = now.getDayOfWeek();
        boolean isWeekday = (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY);
        int hour = now.getHour();
        boolean isPeak = isWeekday && ((hour >= 6 && hour < 9) || (hour >= 17 && hour < 20));

        var dayType = isWeekday ? DayType.WEEKDAY : DayType.WEEKEND;
        var timePeriod = isPeak ? TimePeriod.PEAK : TimePeriod.OFFPEAK;

        var fare = fareReadService.findFareForRouteTerminals(
                route.getEntityId(),
                boardingTerminal.getEntityId(),
                dropoffTerminal.getEntityId(),
                dayType,
                timePeriod
        ).orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "driver.fare.not.found"));

        var payment = new PassengerPayment();
        payment.setDriver(driver);
        payment.setRoute(route);
        payment.setBoardingTerminal(boardingTerminal);
        payment.setDropoffTerminal(dropoffTerminal);
        payment.setPassengerPhone(form.getPassengerPhone().trim());
        payment.setFareAmount(fare.getFareAmount());
        payment.setPromptStatus("STK_SENT");
        payment.setPromptRequestedAt(now);
        payment.setCreatedByEntityId(driverId);
        payment = passengerPaymentRepository.save(payment);

        try {
            smsService.send(
                    payment.getPassengerPhone(),
                    "M-Pesa payment prompt for KES " + payment.getFareAmount()
                            + " initiated for trip on " + route.getName()
                            + " to " + dropoffTerminal.getName()
                            + ". Please enter your M-Pesa PIN."
            );
        } catch (Exception e) {
            // Keep going even if SMS fails in dev/test
        }

        return payment;
    }

    @Transactional
    public PassengerPayment clearDropoff(String driverId, String paymentId) {
        var payment = passengerPaymentRepository.findByEntityId(paymentId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        if (payment.getDriver() == null || !payment.getDriver().getEntityId().equals(driverId)) {
            throw new CommonRuntimeException(ExceptionType.FORBIDDEN, "error.forbidden");
        }

        payment.setClearedAt(LocalDateTime.now());
        payment.setUpdatedByEntityId(driverId);
        return passengerPaymentRepository.save(payment);
    }

    @Transactional
    public SchoolTransportNotification sendSchoolUpdate(String driverId, String tripId, SchoolTransportUpdateForm form) {
        var trip = tripReadService.findByEntityId(tripId);
        if (trip.getAssignedDriver() == null || !trip.getAssignedDriver().getEntityId().equals(driverId)) {
            throw new CommonRuntimeException(ExceptionType.FORBIDDEN, "error.trip.not.assigned.to.driver");
        }

        var driver = findByEntityId(driverId);
        var schoolAdmin = trip.getSchoolAdmin();
        if (schoolAdmin == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        var notification = new SchoolTransportNotification();
        notification.setSchoolAdmin(schoolAdmin);
        notification.setDriver(driver);
        notification.setTrip(trip);
        notification.setSchoolName(trip.getSchoolName());
        notification.setLocation(trip.getLocation());
        notification.setSubject(form.getSubject().trim());
        notification.setMessage(form.getMessage().trim());
        notification.setStatus("SENT");
        notification.setSentAt(LocalDateTime.now());
        notification.setCreatedByEntityId(driverId);
        notification = schoolTransportNotificationRepository.save(notification);

        if (schoolAdmin.getPhoneNumber() != null && !schoolAdmin.getPhoneNumber().isBlank()) {
            try {
                smsService.send(
                        schoolAdmin.getPhoneNumber(),
                        "Update from driver " + driver.getName() + " for " + trip.getSchoolName()
                                + " (" + form.getSubject() + "): " + form.getMessage()
                );
            } catch (Exception e) {
                // Keep going if SMS broker is unavailable in dev
            }
        }

        return notification;
    }

    @Autowired
    public void setPassengerPaymentRepository(PassengerPaymentRepository passengerPaymentRepository) {
        this.passengerPaymentRepository = passengerPaymentRepository;
    }

    @Autowired
    public void setSchoolTransportNotificationRepository(SchoolTransportNotificationRepository schoolTransportNotificationRepository) {
        this.schoolTransportNotificationRepository = schoolTransportNotificationRepository;
    }

    @Autowired
    public void setRouteReadService(RouteReadService routeReadService) {
        this.routeReadService = routeReadService;
    }

    @Autowired
    public void setTerminalReadService(TerminalReadService terminalReadService) {
        this.terminalReadService = terminalReadService;
    }

    @Autowired
    public void setFareReadService(FareReadService fareReadService) {
        this.fareReadService = fareReadService;
    }

    @Autowired
    public void setTripReadService(TripReadService tripReadService) {
        this.tripReadService = tripReadService;
    }

    @Autowired
    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }
}
