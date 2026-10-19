package com.transport.Trips.view;

import com.transport.Trips.entity.DailyPickupTrip;
import java.time.LocalTime;

public class DailyPickupTripView extends TripView {

    private final DailyPickupTrip dailyPickupTrip;

    public DailyPickupTripView(DailyPickupTrip entity) {
        super(entity);
        this.dailyPickupTrip = entity;
    }

    public String getRouteId() {
        return dailyPickupTrip.getRoute() == null ? null : dailyPickupTrip.getRoute().getEntityId();
    }

    public String getRouteName() {
        return dailyPickupTrip.getRoute() == null ? null : dailyPickupTrip.getRoute().getName();
    }

    public LocalTime getPickupTime() {
        return dailyPickupTrip.getPickupTime();
    }

    public LocalTime getDropoffTime() {
        return dailyPickupTrip.getDropoffTime();
    }
}
