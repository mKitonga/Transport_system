package com.transport.school.SchoolAdmin.Service;

import com.transport.complaints.entity.ComplaintStatus;
import com.transport.complaints.repository.ComplaintsRepository;
import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.BaseJpaRepoReadService;
import com.transport.parents.repository.ParentRepository;
import com.transport.parents.view.ParentView;
import com.transport.sacco.Repository.SaccoTransportRequestRepository;
import com.transport.sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.sacco.SaccoAdmin.Repository.SaccoAdminRepository;
import com.transport.sacco.SaccoAdmin.View.SaccoAdminView;
import com.transport.sacco.View.SaccoTransportRequestView;
import com.transport.sacco.SaccoDriver.Repository.SchoolTransportNotificationRepository;
import com.transport.sacco.SaccoDriver.View.SchoolTransportNotificationView;
import com.transport.school.Entity.SchoolTripPayment;
import com.transport.school.Repository.SchoolTripPaymentRepository;
import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.school.SchoolAdmin.Repository.SchoolAdminRepository;
import com.transport.school.SchoolAdmin.View.SchoolAdminOverviewView;
import com.transport.school.SchoolAdmin.View.SchoolDriverView;
import com.transport.school.SchoolDriver.Repository.SchoolDriverNotificationRepository;
import com.transport.school.SchoolDriver.Repository.SchoolDriverRepository;
import com.transport.school.SchoolDriver.View.SchoolDriverNotificationView;
import com.transport.school.View.SchoolTripPaymentView;
import com.transport.trips.repository.TripRepository;
import com.transport.user.entity.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SchoolAdminReadService extends BaseJpaRepoReadService<SchoolAdmin, SchoolAdminRepository> {

    private SchoolDriverRepository schoolDriverRepository;
    private SchoolTransportNotificationRepository notificationRepository;
    private SchoolDriverNotificationRepository driverNotificationRepository;
    private ComplaintsRepository complaintsRepository;
    private TripRepository tripRepository;
    private ParentRepository parentRepository;
    private SaccoAdminRepository saccoAdminRepository;
    private SaccoTransportRequestRepository transportRequestRepository;
    private SchoolTripPaymentRepository paymentRepository;

    public Optional<SchoolAdmin> findActiveAdminByEntityId(String entityId) {
        return repository.findByEntityIdAndDeletedAtIsNull(entityId);
    }

    @Transactional(readOnly = true)
    public SchoolAdminOverviewView getOverview(String adminEntityId) {
        var admin = findActiveAdminByEntityId(adminEntityId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        String schoolId = admin.getSchool() != null ? admin.getSchool().getEntityId() : null;

        long pendingDriverApprovals = schoolId == null ? 0 :
                schoolDriverRepository.count(
                        schoolDriverRepository.notDeleted()
                                .and(schoolDriverRepository.schoolEntityIdIs(schoolId))
                                .and(schoolDriverRepository.userStatusIs(UserStatus.PENDING_APPROVAL))
                );

        long openComplaints = schoolId == null ? 0 :
                complaintsRepository.count(
                        complaintsRepository.notDeleted()
                                .and(complaintsRepository.schoolEntityIdIs(schoolId))
                                .and(complaintsRepository.statusIs(ComplaintStatus.OPEN))
                );

        long totalTrips = tripRepository.count(
                tripRepository.notDeleted()
                        .and(tripRepository.schoolAdminEntityIdIs(adminEntityId))
        );

        long unreadNotifications = notificationRepository.count(
                notificationRepository.notDeleted()
                        .and(notificationRepository.schoolAdminEntityIdIs(adminEntityId))
                        .and(notificationRepository.statusIs("SENT"))
        );

        return SchoolAdminOverviewView.builder()
                .pendingDriverApprovals(pendingDriverApprovals)
                .openComplaints(openComplaints)
                .totalTrips(totalTrips)
                .unreadNotifications(unreadNotifications)
                .build();
    }

    @Transactional(readOnly = true)
    public Page<SchoolDriverView> listSchoolDrivers(String adminEntityId, String query, UserStatus status, BaseFetchForm form) {
        var admin = findActiveAdminByEntityId(adminEntityId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        if (admin.getSchool() == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        var spec = schoolDriverRepository.notDeleted()
                .and(schoolDriverRepository.schoolEntityIdIs(admin.getSchool().getEntityId()));

        if (query != null && !query.isBlank()) {
            spec = spec.and(schoolDriverRepository.nameLike(query));
        }
        if (status != null) {
            spec = spec.and(schoolDriverRepository.userStatusIs(status));
        }

        return schoolDriverRepository.findAll(spec, schoolDriverRepository.defaultPageable(form))
                .map(SchoolDriverView::new);
    }

    @Transactional(readOnly = true)
    public Page<SchoolTransportNotificationView> listNotifications(String adminEntityId, BaseFetchForm form) {
        var spec = notificationRepository.notDeleted()
                .and(notificationRepository.schoolAdminEntityIdIs(adminEntityId));

        return notificationRepository.findAll(spec, notificationRepository.defaultPageable(form))
                .map(SchoolTransportNotificationView::new);
    }

    @Transactional(readOnly = true)
    public Page<ParentView> listParents(String adminEntityId, String query, BaseFetchForm form) {
        var admin = findActiveAdminByEntityId(adminEntityId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        if (admin.getSchool() == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        var spec = parentRepository.notDeleted()
                .and(parentRepository.schoolEntityIdIs(admin.getSchool().getEntityId()));

        if (query != null && !query.isBlank()) {
            spec = spec.and(parentRepository.nameLike(query));
        }

        return parentRepository.findAll(spec, parentRepository.defaultPageable(form))
                .map(ParentView::new);
    }

    @Transactional(readOnly = true)
    public Page<SaccoAdminView> listSaccoDirectory(String query, BaseFetchForm form) {
        var spec = saccoAdminRepository.notDeleted()
                .and(saccoAdminRepository.userStatusIs(UserStatus.ACTIVE));

        if (query != null && !query.isBlank()) {
            spec = spec.and(saccoAdminRepository.nameLike(query));
        }

        return saccoAdminRepository.findAll(spec, saccoAdminRepository.defaultPageable(form))
                .map(SaccoAdminView::new);
    }

    @Transactional(readOnly = true)
    public Page<SaccoTransportRequestView> listTransportRequests(String adminEntityId, BaseFetchForm form) {
        var spec = transportRequestRepository.notDeleted()
                .and(transportRequestRepository.schoolAdminEntityIdIs(adminEntityId));

        return transportRequestRepository.findAll(spec, transportRequestRepository.defaultPageable(form))
                .map(SaccoTransportRequestView::new);
    }

    @Transactional(readOnly = true)
    public Page<SchoolDriverNotificationView> listDriverAlerts(String adminEntityId, BaseFetchForm form) {
        var spec = driverNotificationRepository.notDeleted()
                .and(driverNotificationRepository.schoolAdminEntityIdIs(adminEntityId));

        return driverNotificationRepository.findAll(spec, driverNotificationRepository.defaultPageable(form))
                .map(SchoolDriverNotificationView::new);
    }

    @Transactional(readOnly = true)
    public Page<SchoolTripPaymentView> listPayments(String adminEntityId, String status, BaseFetchForm form) {
        var spec = paymentRepository.notDeleted()
                .and(paymentRepository.schoolAdminEntityIdIs(adminEntityId));

        if (status != null && !status.isBlank()) {
            spec = spec.and(paymentRepository.statusIs(status.trim().toUpperCase()));
        }

        return paymentRepository.findAll(spec, paymentRepository.defaultPageable(form))
                .map(SchoolTripPaymentView::new);
    }

    @Autowired
    public void setSchoolDriverRepository(SchoolDriverRepository schoolDriverRepository) {
        this.schoolDriverRepository = schoolDriverRepository;
    }

    @Autowired
    public void setNotificationRepository(SchoolTransportNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Autowired
    public void setDriverNotificationRepository(SchoolDriverNotificationRepository driverNotificationRepository) {
        this.driverNotificationRepository = driverNotificationRepository;
    }

    @Autowired
    public void setComplaintsRepository(ComplaintsRepository complaintsRepository) {
        this.complaintsRepository = complaintsRepository;
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
    public void setSaccoAdminRepository(SaccoAdminRepository saccoAdminRepository) {
        this.saccoAdminRepository = saccoAdminRepository;
    }

    @Autowired
    public void setTransportRequestRepository(SaccoTransportRequestRepository transportRequestRepository) {
        this.transportRequestRepository = transportRequestRepository;
    }

    @Autowired
    public void setPaymentRepository(SchoolTripPaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }
}
