package com.transport.trips.view;

import com.transport.trips.entity.ClosingDayTrip;
import com.transport.trips.entity.DailyPickupTrip;
import com.transport.trips.entity.EducationalTrip;
import com.transport.trips.entity.Trip;
import com.transport.school.Entity.SchoolGrade;
import com.transport.trips.entity.TripStatus;
import com.transport.trips.entity.TripType;
import com.transport.liby.view.BaseView;
import java.math.BigDecimal;

public abstract class TripView extends BaseView<Trip> {

    protected TripView(Trip entity) {
        super(entity);
    }

    public String getSchoolAdminId() {
        return entity.getSchoolAdmin() == null ? null : entity.getSchoolAdmin().getEntityId();
    }

    public String getSchoolName() {
        return entity.getSchoolName();
    }

    public String getLocation() {
        return entity.getLocation();
    }

    public TripStatus getStatus() {
        return entity.getStatus();
    }

    public TripType getTripType() {
        return entity.getTripType();
    }

    public SchoolGrade getGrade() {
        return entity.getGrade();
    }

    public BigDecimal getAmount() {
        return entity.getAmount();
    }

    public String getAssignedDriverId() {
        return entity.getAssignedDriver() == null ? null : entity.getAssignedDriver().getEntityId();
    }

    public String getAssignedDriverName() {
        return entity.getAssignedDriver() == null ? null : entity.getAssignedDriver().getName();
    }

    public static TripView toView(Trip trip) {
        if (trip instanceof DailyPickupTrip dailyPickupTrip) {
            return new DailyPickupTripView(dailyPickupTrip);
        }
        if (trip instanceof EducationalTrip educationalTrip) {
            return new EducationalTripView(educationalTrip);
        }
        if (trip instanceof ClosingDayTrip closingDayTrip) {
            return new ClosingDayTripView(closingDayTrip);
        }
        throw new IllegalArgumentException("Unsupported trip type: " + (trip != null ? trip.getClass().getName() : "null"));
    }
}
