package com.transport.sacco.SaccoDriver.View;

import com.transport.sacco.SaccoDriver.Entity.SaccoDriver;
import com.transport.user.view.UserView;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SaccoDriverView extends UserView<SaccoDriver> {

    public SaccoDriverView(SaccoDriver entity) {
        super(entity);
    }

    public String getSaccoId() {
        return entity.getSacco() != null ? entity.getSacco().getEntityId() : null;
    }

    public String getSaccoName() {
        return entity.getSacco() != null ? entity.getSacco().getName() : null;
    }

    public String getAssignedRouteId() {
        return entity.getAssignedRoute() != null ? entity.getAssignedRoute().getEntityId() : null;
    }

    public String getAssignedRouteName() {
        return entity.getAssignedRoute() != null ? entity.getAssignedRoute().getName() : null;
    }

    public String getNumberPlate() {
        return entity.getNumberPlate();
    }

    public String getMatatuName() {
        return entity.getMatatuName();
    }
}
