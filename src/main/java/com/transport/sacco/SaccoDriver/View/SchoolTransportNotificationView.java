package com.transport.sacco.SaccoDriver.View;

import com.transport.liby.view.BaseView;
import com.transport.sacco.SaccoDriver.Entity.SchoolTransportNotification;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SchoolTransportNotificationView extends BaseView<SchoolTransportNotification> {

    public SchoolTransportNotificationView(SchoolTransportNotification entity) {
        super(entity);
    }

    public String getSchoolAdminId() {
        return entity.getSchoolAdmin() != null ? entity.getSchoolAdmin().getEntityId() : null;
    }

    public String getDriverId() {
        return entity.getDriver() != null ? entity.getDriver().getEntityId() : null;
    }

    public String getDriverName() {
        return entity.getDriver() != null ? entity.getDriver().getName() : null;
    }

    public String getTripId() {
        return entity.getTrip() != null ? entity.getTrip().getEntityId() : null;
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
