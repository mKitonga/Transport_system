package com.transport.school.SchoolDriver.Service;

import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.form.BaseFetchForm;
import com.transport.liby.service.BaseJpaRepoReadService;
import com.transport.school.SchoolAdmin.Repository.SchoolAdminRepository;
import com.transport.school.SchoolDriver.Entity.SchoolDriver;
import com.transport.school.SchoolDriver.Repository.SchoolDriverAssignmentRepository;
import com.transport.school.SchoolDriver.Repository.SchoolDriverNotificationRepository;
import com.transport.school.SchoolDriver.Repository.SchoolDriverRepository;
import com.transport.school.SchoolDriver.View.SchoolDriverAssignmentView;
import com.transport.school.SchoolDriver.View.SchoolDriverNotificationView;
import com.transport.school.SchoolDriver.View.SchoolDriverOverviewView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class SchoolDriverReadService extends BaseJpaRepoReadService<SchoolDriver, SchoolDriverRepository> {

    private SchoolDriverAssignmentRepository assignmentRepository;
    private SchoolDriverNotificationRepository notificationRepository;

    public Optional<SchoolDriver> findActiveByEntityId(String entityId) {
        return repository.findByEntityIdAndDeletedAtIsNull(entityId);
    }

    @Transactional(readOnly = true)
    public SchoolDriverOverviewView getOverview(String driverId) {
        var driver = findActiveByEntityId(driverId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        long totalAssignments = assignmentRepository.count(
                assignmentRepository.notDeleted()
                        .and(assignmentRepository.schoolDriverEntityIdIs(driverId))
        );

        long sentAlerts = notificationRepository.count(
                notificationRepository.notDeleted()
                        .and(notificationRepository.schoolDriverEntityIdIs(driverId))
        );

        return SchoolDriverOverviewView.builder()
                .driverName(driver.getName())
                .schoolName(driver.getSchool() != null ? driver.getSchool().getName() : null)
                .location(driver.getLocation())
                .totalAssignments(totalAssignments)
                .sentAlerts(sentAlerts)
                .build();
    }

    @Transactional(readOnly = true)
    public Page<SchoolDriverAssignmentView> listAssignments(String driverId, BaseFetchForm form) {
        var spec = assignmentRepository.notDeleted()
                .and(assignmentRepository.schoolDriverEntityIdIs(driverId));

        return assignmentRepository.findAll(spec, assignmentRepository.defaultPageable(form))
                .map(SchoolDriverAssignmentView::new);
    }

    @Transactional(readOnly = true)
    public Page<SchoolDriverNotificationView> listMyAlerts(String driverId, BaseFetchForm form) {
        var spec = notificationRepository.notDeleted()
                .and(notificationRepository.schoolDriverEntityIdIs(driverId));

        return notificationRepository.findAll(spec, notificationRepository.defaultPageable(form))
                .map(SchoolDriverNotificationView::new);
    }

    @Autowired
    public void setAssignmentRepository(SchoolDriverAssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Autowired
    public void setNotificationRepository(SchoolDriverNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }
}
