package com.transport.sacco.View;

import com.transport.liby.view.BaseView;
import com.transport.sacco.Entity.SaccoTransportRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SaccoTransportRequestView extends BaseView<SaccoTransportRequest> {

    public SaccoTransportRequestView(SaccoTransportRequest entity) {
        super(entity);
    }

    public String getSchoolAdminId() {
        return entity.getSchoolAdmin() != null ? entity.getSchoolAdmin().getEntityId() : null;
    }

    public String getSchoolAdminName() {
        return entity.getSchoolAdmin() != null ? entity.getSchoolAdmin().getName() : null;
    }

    public String getSaccoAdminId() {
        return entity.getSaccoAdmin() != null ? entity.getSaccoAdmin().getEntityId() : null;
    }

    public String getSaccoAdminName() {
        return entity.getSaccoAdmin() != null ? entity.getSaccoAdmin().getName() : null;
    }

    public String getSchoolName() {
        return entity.getSchoolName();
    }

    public String getLocation() {
        return entity.getLocation();
    }

    public int getRequestedVehicles() {
        return entity.getRequestedVehicles();
    }

    public String getNotes() {
        return entity.getNotes();
    }

    public String getStatus() {
        return entity.getStatus();
    }

    public LocalDateTime getRequestedAt() {
        return entity.getRequestedAt();
    }
}
