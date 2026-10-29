package com.transport.trips.view;

import com.transport.trips.entity.EducationalTrip;
import java.time.LocalDate;

public class EducationalTripView extends TripView {

    private final EducationalTrip educationalTrip;

    public EducationalTripView(EducationalTrip entity) {
        super(entity);
        this.educationalTrip = entity;
    }

    public String getTripTitle() {
        return educationalTrip.getTripTitle();
    }

    public String getDestination() {
        return educationalTrip.getDestination();
    }

    public int getDurationDays() {
        return educationalTrip.getDurationDays();
    }

    public LocalDate getTripDate() {
        return educationalTrip.getTripDate();
    }

    public String getSaccoAdminId() {
        return educationalTrip.getSaccoAdmin() == null
                ? null
                : educationalTrip.getSaccoAdmin().getEntityId();
    }

    public String getSaccoId() {
        return educationalTrip.getSacco() == null
                ? null
                : educationalTrip.getSacco().getEntityId();
    }
}
