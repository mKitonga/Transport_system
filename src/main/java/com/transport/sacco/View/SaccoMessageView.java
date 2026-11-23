package com.transport.sacco.View;

import com.transport.liby.view.BaseView;
import com.transport.sacco.Entity.SaccoMessage;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SaccoMessageView extends BaseView<SaccoMessage> {

    public SaccoMessageView(SaccoMessage entity) {
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
