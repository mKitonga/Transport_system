package com.transport.Routes.View;

import com.transport.Routes.Entity.Route;
import com.transport.liby.view.BaseView;

import java.util.List;

public class RouteView extends BaseView<Route> {
    private final String name;
    private final String ownerType;
    private final String ownerId;
    private final List<TerminalView> terminals;

    public RouteView(Route entity) {
        super(entity);
        this.name = entity.getName();
        this.ownerType = entity.getSacco() != null ? "SACCO" : "SCHOOL";
        this.ownerId = entity.getSacco() != null
                ? entity.getSacco().getEntityId()
                : entity.getSchool().getEntityId();
        this.terminals = entity.getTerminals().stream()
                .map(TerminalView::new)
                .toList();
    }

    public String getName() {
        return name;
    }

    public String getOwnerType() {
        return ownerType;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public List<TerminalView> getTerminals() {
        return terminals;
    }
}
