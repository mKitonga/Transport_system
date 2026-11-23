package com.transport.school.SchoolDriver.View;

import com.transport.liby.view.BaseView;
import com.transport.school.SchoolDriver.Entity.SchoolDriverAssignment;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Getter
@Setter
public class SchoolDriverAssignmentView extends BaseView<SchoolDriverAssignment> {

    public SchoolDriverAssignmentView(SchoolDriverAssignment entity) {
        super(entity);
    }

    public String getSchoolDriverId() {
        return entity.getSchoolDriver() != null ? entity.getSchoolDriver().getEntityId() : null;
    }

    public String getTripId() {
        return entity.getDailyPickupTrip() != null ? entity.getDailyPickupTrip().getEntityId() : null;
    }

    public String getRouteName() {
        if (entity.getDailyPickupTrip() == null) return null;
        var route = entity.getDailyPickupTrip().getRoute();
        return route != null ? route.getName() : null;
    }

    public String getSchoolGrade() {
        if (entity.getDailyPickupTrip() == null) return null;
        var grade = entity.getDailyPickupTrip().getGrade();
        return grade != null ? grade.name() : null;
    }

    public LocalTime getPickupTime() {
        return entity.getDailyPickupTrip() != null ? entity.getDailyPickupTrip().getPickupTime() : null;
    }

    public LocalTime getDropoffTime() {
        return entity.getDailyPickupTrip() != null ? entity.getDailyPickupTrip().getDropoffTime() : null;
    }

    public LocalTime getAssignedPickupTime() {
        return entity.getAssignedPickupTime();
    }

    public String getStudentName() {
        return entity.getStudentName();
    }

    public String getParentId() {
        return entity.getParent() != null ? entity.getParent().getEntityId() : null;
    }

    public String getParentName() {
        return entity.getParent() != null ? entity.getParent().getName() : null;
    }

    public String getParentPhone() {
        return entity.getParent() != null ? entity.getParent().getPhoneNumber() : null;
    }

    public String getHomeLocation() {
        return entity.getHomeLocation();
    }
}
