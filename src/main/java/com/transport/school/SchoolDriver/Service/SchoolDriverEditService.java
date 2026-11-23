package com.transport.school.SchoolDriver.Service;

import com.transport.liby.exception.CommonRuntimeException;
import com.transport.liby.exception.ExceptionType;
import com.transport.liby.service.BaseJpaRepoEditService;
import com.transport.notification.sms.SmsService;
import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.school.SchoolAdmin.Repository.SchoolAdminRepository;
import com.transport.school.SchoolDriver.Entity.SchoolDriver;
import com.transport.school.SchoolDriver.Entity.SchoolDriverNotification;
import com.transport.school.SchoolDriver.Form.SendSchoolDriverAlertForm;
import com.transport.school.SchoolDriver.Repository.SchoolDriverNotificationRepository;
import com.transport.school.SchoolDriver.Repository.SchoolDriverRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SchoolDriverEditService extends BaseJpaRepoEditService<SchoolDriver, SchoolDriverRepository> {

    private SchoolDriverReadService schoolDriverReadService;
    private SchoolAdminRepository schoolAdminRepository;
    private SchoolDriverNotificationRepository notificationRepository;
    private SmsService smsService;

    @Transactional
    public List<SchoolDriverNotification> sendAlert(String driverId, SendSchoolDriverAlertForm form) {
        var driver = schoolDriverReadService.findActiveByEntityId(driverId)
                .orElseThrow(() -> new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found"));

        if (driver.getSchool() == null) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.entity.not.found");
        }

        List<SchoolAdmin> admins = schoolAdminRepository.findAll(
                schoolAdminRepository.notDeleted()
                        .and(schoolAdminRepository.schoolEntityIdIs(driver.getSchool().getEntityId()))
        );

        if (admins.isEmpty()) {
            throw new CommonRuntimeException(ExceptionType.NOT_FOUND, "error.school.admin.not.found");
        }

        List<SchoolDriverNotification> notifications = admins.stream().map(admin -> {
            SchoolDriverNotification notification = new SchoolDriverNotification();
            notification.setSchoolDriver(driver);
            notification.setSchoolAdmin(admin);
            notification.setSchoolName(driver.getSchool().getName());
            notification.setLocation(driver.getLocation());
            notification.setSubject(form.getSubject().trim());
            notification.setMessage(form.getMessage().trim());
            notification.setCreatedByEntityId(driverId);

            if (admin.getPhoneNumber() != null && !admin.getPhoneNumber().isBlank()) {
                try {
                    smsService.send(
                            admin.getPhoneNumber(),
                            "Alert from school driver " + driver.getName()
                                    + " [" + form.getSubject() + "]: " + form.getMessage()
                    );
                } catch (Exception ignored) {

                }
            }
            return notification;
        }).toList();

        return notificationRepository.saveAll(notifications);
    }

    @Autowired
    public void setSchoolDriverReadService(SchoolDriverReadService schoolDriverReadService) {
        this.schoolDriverReadService = schoolDriverReadService;
    }

    @Autowired
    public void setSchoolAdminRepository(SchoolAdminRepository schoolAdminRepository) {
        this.schoolAdminRepository = schoolAdminRepository;
    }

    @Autowired
    public void setNotificationRepository(SchoolDriverNotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Autowired
    public void setSmsService(SmsService smsService) {
        this.smsService = smsService;
    }
}
