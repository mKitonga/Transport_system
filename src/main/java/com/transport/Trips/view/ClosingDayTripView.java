package com.transport.Trips.view;

import com.transport.Trips.entity.ClosingDayTrip;
import java.time.LocalDate;

public class ClosingDayTripView extends TripView {

    private final ClosingDayTrip closingDayTrip;

    public ClosingDayTripView(ClosingDayTrip entity) {
        super(entity);
        this.closingDayTrip = entity;
    }

    public String getRouteId() {
        return closingDayTrip.getRoute() == null ? null : closingDayTrip.getRoute().getEntityId();
    }

    public String getRouteName() {
        return closingDayTrip.getRoute() == null ? null : closingDayTrip.getRoute().getName();
    }

    public LocalDate getTripDate() {
        return closingDayTrip.getTripDate();
    }

    public String getSaccoAdminId() {
        return closingDayTrip.getSaccoAdmin() == null
                ? null
                : closingDayTrip.getSaccoAdmin().getEntityId();
    }

    public String getSaccoId() {
        return closingDayTrip.getSaccoName() == null
                ? null
                : closingDayTrip.getSaccoName().getEntityId();
    }
}
