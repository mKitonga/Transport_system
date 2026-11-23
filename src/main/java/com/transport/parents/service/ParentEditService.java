package com.transport.parents.service;

import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoEditService;
import com.transport.notification.sms.SmsService;
import com.transport.parents.entity.Parent;
import com.transport.parents.form.MakeSchoolTripPaymentForm;
import com.transport.parents.repository.ParentRepository;
import com.transport.school.Entity.SchoolTripPayment;
import com.transport.school.Repository.SchoolTripPaymentRepository;
import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.trips.entity.Trip;
import com.transport.trips.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Locale;
import java.util.UUID;

@Service
public class ParentEditService extends BaseJpaRepoEditService<Parent, ParentRepository> {

    private ParentReadService parentReadService;
    private TripRepository tripRepository;
    private SchoolTripPaymentRepository paymentRepository;
    private SmsService smsService;

    @Transactional
    public SchoolTripPayment makePayment(String parentId, MakeSchoolTripPaymentForm form) {
        var parent = parentReadService.findActiveParentByEntityId(parentId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        if (parent.getSchool() == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        String normalizedType = form.getTripType().trim().toUpperCase(Locale.ROOT);
        if (!normalizedType.equals("DAILY") && !normalizedType.equals("EDUCATIONAL") && !normalizedType.equals("CLOSING")) {
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.trip.type.invalid");
        }

        var trip = tripRepository.findByEntityId(form.getTripId())
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.trip.not.found"));

        if (trip.getSchoolAdmin() == null || trip.getSchoolAdmin().getSchool() == null
                || !trip.getSchoolAdmin().getSchool().getEntityId().equals(parent.getSchool().getEntityId())) {
            throw new CommonRuntimeException(ExceptionType.FORBIDDEN, "error.trip.not.for.student");
        }

        if (!normalizedType.equals("CLOSING") && trip.getGrade() != null && parent.getGrade() != null
                && !trip.getGrade().equals(parent.getGrade())) {
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.trip.grade.mismatch");
        }

        var schoolAdmin = trip.getSchoolAdmin();
        BigDecimal amount = trip.getAmount() != null ? trip.getAmount() : BigDecimal.ZERO;
        String mpesaRef = "STS" + UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase(Locale.ROOT);

        var payment = paymentRepository
                .findByParent_EntityIdAndTripTypeAndTripId(parentId, normalizedType, trip.getEntityId())
                .orElseGet(SchoolTripPayment::new);

        payment.setParent(parent);
        payment.setSchoolAdmin(schoolAdmin);
        payment.setTripType(normalizedType);
        payment.setTripId(trip.getEntityId());
        payment.setStudentName(parent.getStudentName());
        payment.setGrade(parent.getGrade());
        payment.setAmount(amount);
        payment.setPhoneNumber(form.getPhoneNumber().trim());
        payment.setMpesaReference(mpesaRef);
        payment.setStatus("SUBMITTED");
        payment.setCreatedByEntityId(parentId);

        payment = paymentRepository.save(payment);

        try {
            smsService.send(
                    payment.getPhoneNumber(),
                    "M-Pesa payment prompt of KES " + amount + " initiated for " + normalizedType
                            + " trip at " + parent.getSchoolName() + " (Ref: " + mpesaRef + "). Please enter your M-Pesa PIN."
            );
        } catch (Exception ignored) {

        }

        return payment;
    }

    @Autowired
    public void setParentReadService(ParentReadService parentReadService) {
        this.parentReadService = parentReadService;
    }

    @Autowired
    public void setTripRepository(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Autowired
    public void setPaymentRepository(SchoolTripPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Autowired
    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }
}
