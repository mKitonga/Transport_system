package com.transport.trips.entity;

import com.transport.sacco.Entity.Sacco;
import com.transport.sacco.SaccoAdmin.Entity.SaccoAdmin;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "educational_trips")
@Getter
@Setter
public class EducationalTrip extends Trip {

    @Column(name = "trip_title", nullable = false)
    private String tripTitle;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "duration_days", nullable = false)
    private int durationDays;

    @Column(name = "trip_date")
    private LocalDate tripDate;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "sacco_admin_id", nullable = true)
    private SaccoAdmin saccoAdmin;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "sacco_id", nullable = true)
    private Sacco sacco;

    public EducationalTrip() {
        setTripType(TripType.EDUCATIONAL_TRIP);
    }
}
