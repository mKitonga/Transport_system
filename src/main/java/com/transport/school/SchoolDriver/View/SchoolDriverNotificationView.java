package com.transport.school.SchoolDriver.View;

import com.transport.liby.view.BaseView;
import com.transport.school.SchoolDriver.Entity.SchoolDriverNotification;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SchoolDriverNotificationView extends BaseView<SchoolDriverNotification> {

    public SchoolDriverNotificationView(SchoolDriverNotification entity) {
        super(entity);
    }

    public String getSchoolDriverId() {
        return entity.getSchoolDriver() != null ? entity.getSchoolDriver().getEntityId() : null;
    }

    public String getSchoolDriverName() {
        return entity.getSchoolDriver() != null ? entity.getSchoolDriver().getName() : null;
    }

    public String getSchoolAdminId() {
        return entity.getSchoolAdmin() != null ? entity.getSchoolAdmin().getEntityId() : null;
    }

    public String getSchoolName() {
        return entity.getSchoolName();
    }

    public String getLocation() {
        return entity.getLocation();
    }

    public String getSubject() {
        return entity.getSubject();
    }

    public String getMessage() {
        return entity.getMessage();
    }

    public String getStatus() {
        return entity.getStatus();
    }

    public LocalDateTime getSentAt() {
        return entity.getSentAt();
    }
}
