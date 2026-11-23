package com.transport.sacco.SaccoAdmin.View;

import com.transport.sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.user.view.UserView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaccoAdminView extends UserView<SaccoAdmin> {

    public SaccoAdminView(SaccoAdmin entity) {
        super(entity);
    }

    public String getSaccoId() {
        return entity.getSacco() != null ? entity.getSacco().getEntityId() : null;
    }

    public String getSaccoName() {
        return entity.getSacco() != null ? entity.getSacco().getName() : null;
    }

    public String getStationId() {
        return entity.getStation() != null ? entity.getStation().getEntityId() : null;
    }

    public String getStationName() {
        return entity.getStation() != null ? entity.getStation().getName() : null;
    }
}
