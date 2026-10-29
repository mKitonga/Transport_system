package com.transport.trips.view;

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
}
