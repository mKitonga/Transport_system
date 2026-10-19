package com.transport.Trips.entity;

import com.transport.Routes.Entity.Route;
import com.transport.Sacco.Entity.Sacco;
import com.transport.Sacco.SaccoAdmin.Entity.SaccoAdmin;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "closing_day_trips")
@Getter
@Setter
public class ClosingDayTrip extends Trip {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "trip_date", nullable = false)
    private LocalDate tripDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sacco_admin_id", nullable = false)
    private SaccoAdmin saccoAdmin;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sacco_id")
    private Sacco saccoName;

    public ClosingDayTrip() {
        setTripType(TripType.CLOSING_DAY_TRIP);
    }
}
