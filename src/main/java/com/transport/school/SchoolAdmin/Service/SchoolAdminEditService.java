package com.transport.school.SchoolAdmin.Service;

import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoEditService;
import com.transport.parents.entity.Parent;
import com.transport.parents.repository.ParentRepository;
import com.transport.sacco.Entity.SaccoMessage;
import com.transport.sacco.Entity.SaccoTransportRequest;
import com.transport.sacco.Repository.SaccoMessageRepository;
import com.transport.sacco.Repository.SaccoTransportRequestRepository;
import com.transport.sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.sacco.SaccoAdmin.Repository.SaccoAdminRepository;
import com.transport.school.Entity.SchoolTripPayment;
import com.transport.school.Repository.SchoolTripPaymentRepository;
import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.school.SchoolAdmin.Form.AssignDailyTripStudentsForm;
import com.transport.school.SchoolAdmin.Form.CreateSaccoTransportRequestForm;
import com.transport.school.SchoolAdmin.Form.SendSaccoMessageForm;
import com.transport.school.SchoolAdmin.Repository.SchoolAdminRepository;
import com.transport.school.SchoolDriver.Entity.SchoolDriver;
import com.transport.school.SchoolDriver.Entity.SchoolDriverAssignment;
import com.transport.school.SchoolDriver.Repository.SchoolDriverAssignmentRepository;
import com.transport.school.SchoolDriver.Repository.SchoolDriverRepository;
import com.transport.trips.entity.DailyPickupTrip;
import com.transport.trips.entity.Trip;
import com.transport.trips.repository.TripRepository;
import com.transport.user.entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SchoolAdminEditService extends BaseJpaRepoEditService<SchoolAdmin, SchoolAdminRepository> {

    private SchoolAdminReadService schoolAdminReadService;
    private SchoolDriverRepository schoolDriverRepository;
    private SaccoAdminRepository saccoAdminRepository;
    private SaccoTransportRequestRepository transportRequestRepository;
    private SaccoMessageRepository saccoMessageRepository;
    private TripRepository tripRepository;
    private ParentRepository parentRepository;
    private SchoolDriverAssignmentRepository schoolDriverAssignmentRepository;
    private SchoolTripPaymentRepository paymentRepository;

    @Transactional
    public SchoolDriver approveSchoolDriver(String driverId, String schoolAdminId, String updatedById) {
        var admin = schoolAdminReadService.findActiveAdminByEntityId(schoolAdminId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        if (admin.getSchool() == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        var driver = schoolDriverRepository
                .findByEntityIdAndDeletedAtIsNull(driverId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.school.driver.not.found"));

        if (driver.getSchool() == null ||
                !driver.getSchool().getEntityId().equals(admin.getSchool().getEntityId())) {
            throw new CommonRuntimeException(ExceptionType.FORBIDDEN, "error.school.driver.not.found");
        }

        driver.setUserStatus(UserStatus.ACTIVE);
        driver.setUpdatedByEntityId(updatedById);
        return schoolDriverRepository.save(driver);
    }



    @Transactional
    public SaccoTransportRequest createTransportRequest(String adminEntityId,
                                                        CreateSaccoTransportRequestForm form) {
        var admin = schoolAdminReadService.findActiveAdminByEntityId(adminEntityId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        var saccoAdmin = saccoAdminRepository
                .findByEntityIdAndDeletedAtIsNullAndDeletedByEntityIdIsNull(form.getSaccoAdminId())
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.sacco.admin.not.found"));

        var request = new SaccoTransportRequest();
        request.setSchoolAdmin(admin);
        request.setSaccoAdmin(saccoAdmin);
        request.setSchoolName(admin.getSchool() != null ? admin.getSchool().getName() : null);
        request.setLocation(admin.getLocation());
        request.setRequestedVehicles(form.getRequestedVehicles());
        request.setNotes(form.getNotes());
        return transportRequestRepository.save(request);
    }

    @Transactional
    public SaccoMessage sendSaccoMessage(String adminEntityId, SendSaccoMessageForm form) {
        var admin = schoolAdminReadService.findActiveAdminByEntityId(adminEntityId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        var saccoAdmin = saccoAdminRepository
                .findByEntityIdAndDeletedAtIsNullAndDeletedByEntityIdIsNull(form.getSaccoAdminId())
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.sacco.admin.not.found"));

        var msg = new SaccoMessage();
        msg.setSchoolAdmin(admin);
        msg.setSaccoAdmin(saccoAdmin);
        msg.setSchoolName(admin.getSchool() != null ? admin.getSchool().getName() : null);
        msg.setLocation(admin.getLocation());
        msg.setMessage(form.getMessage());
        return saccoMessageRepository.save(msg);
    }

    @Transactional
    public List<SchoolDriverAssignment> assignDailyTripStudents(String adminEntityId,
                                                                String tripId,
                                                                AssignDailyTripStudentsForm form,
                                                                String updatedById) {
        var admin = schoolAdminReadService.findActiveAdminByEntityId(adminEntityId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        if (admin.getSchool() == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        var trip = tripRepository.findByEntityId(tripId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.trip.not.found"));

        if (!(trip instanceof DailyPickupTrip dailyPickupTrip)) {
            throw new CommonRuntimeException(ExceptionType.BAD_REQUEST, "error.trip.not.daily.pickup");
        }

        if (trip.getSchoolAdmin() == null || !trip.getSchoolAdmin().getEntityId().equals(adminEntityId)) {
            throw new CommonRuntimeException(ExceptionType.FORBIDDEN, "error.forbidden");
        }

        var driver = schoolDriverRepository.findByEntityIdAndDeletedAtIsNull(form.getSchoolDriverId())
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.school.driver.not.found"));

        if (driver.getSchool() == null || !driver.getSchool().getEntityId().equals(admin.getSchool().getEntityId())) {
            throw new CommonRuntimeException(ExceptionType.FORBIDDEN, "error.school.driver.not.found");
        }

        List<SchoolDriverAssignment> savedAssignments = new ArrayList<>();
        for (String parentId : form.getParentIds()) {
            Parent parent = parentRepository.findByEntityId(parentId).orElse(null);
            if (parent == null || parent.getSchool() == null || !parent.getSchool().getEntityId().equals(admin.getSchool().getEntityId())) {
                continue;
            }

            var assignment = schoolDriverAssignmentRepository
                    .findByDailyPickupTrip_EntityIdAndParent_EntityId(dailyPickupTrip.getEntityId(), parent.getEntityId())
                    .orElseGet(() -> {
                        SchoolDriverAssignment a = new SchoolDriverAssignment();
                        a.setDailyPickupTrip(dailyPickupTrip);
                        a.setParent(parent);
                        return a;
                    });

            assignment.setSchoolDriver(driver);
            assignment.setStudentName(parent.getStudentName());
            assignment.setAssignedPickupTime(form.getAssignedPickupTime());
            assignment.setHomeLocation(parent.getSchoolLocation());
            assignment.setUpdatedByEntityId(updatedById);

            savedAssignments.add(schoolDriverAssignmentRepository.save(assignment));
        }

        return savedAssignments;
    }

    @Transactional
    public SchoolTripPayment approvePayment(String paymentId, String adminEntityId) {
        var admin = schoolAdminReadService.findActiveAdminByEntityId(adminEntityId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        var payment = paymentRepository.findByEntityId(paymentId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        if (payment.getSchoolAdmin() == null || !payment.getSchoolAdmin().getEntityId().equals(adminEntityId)) {
            throw new CommonRuntimeException(ExceptionType.FORBIDDEN, "error.forbidden");
        }

        payment.setStatus("APPROVED");
        payment.setApprovedAt(java.time.LocalDateTime.now());
        payment.setApprovedByEntityId(adminEntityId);
        payment.setUpdatedByEntityId(adminEntityId);

        return paymentRepository.save(payment);
    }

    @Autowired
    public void setSchoolAdminReadService(SchoolAdminReadService schoolAdminReadService) {
        this.schoolAdminReadService = schoolAdminReadService;
    }

    @Autowired
    public void setSchoolDriverRepository(SchoolDriverRepository schoolDriverRepository) {
        this.schoolDriverRepository = schoolDriverRepository;
    }

    @Autowired
    public void setSaccoAdminRepository(SaccoAdminRepository saccoAdminRepository) {
        this.saccoAdminRepository = saccoAdminRepository;
    }

    @Autowired
    public void setTransportRequestRepository(SaccoTransportRequestRepository transportRequestRepository) {
        this.transportRequestRepository = transportRequestRepository;
    }

    @Autowired
    public void setSaccoMessageRepository(SaccoMessageRepository saccoMessageRepository) {
        this.saccoMessageRepository = saccoMessageRepository;
    }

    @Autowired
    public void setTripRepository(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    @Autowired
    public void setParentRepository(ParentRepository parentRepository) {
        this.parentRepository = parentRepository;
    }

    @Autowired
    public void setSchoolDriverAssignmentRepository(SchoolDriverAssignmentRepository schoolDriverAssignmentRepository) {
        this.schoolDriverAssignmentRepository = schoolDriverAssignmentRepository;
    }

    @Autowired
    public void setPaymentRepository(SchoolTripPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
