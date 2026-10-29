package com.transport.trips.entity;

import com.transport.routes.Entity.Route;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "daily_pickup_trips")
@Getter
@Setter
public class DailyPickupTrip extends Trip {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "pickup_time", nullable = false)
    private LocalTime pickupTime;

    @Column(name = "dropoff_time", nullable = false)
    private LocalTime dropoffTime;

    public DailyPickupTrip() {
        setTripType(TripType.DAILY_PICKUP);
    }
}
