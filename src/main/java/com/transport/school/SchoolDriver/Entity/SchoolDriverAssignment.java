package com.transport.school.SchoolDriver.Entity;

import com.transport.liby.entity.BaseJpaEntity;
import com.transport.parents.entity.Parent;
import com.transport.trips.entity.DailyPickupTrip;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

/**
 * Records a school driver's assignment to a daily pickup trip for a specific student/parent.
 * Equivalent to the PHP school_daily_trip_assignments table.
 */
@Entity
@Table(name = "school_driver_assignments")
@Getter
@Setter
public class SchoolDriverAssignment extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_driver_id", nullable = false)
    private SchoolDriver schoolDriver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "daily_pickup_trip_id", nullable = false)
    private DailyPickupTrip dailyPickupTrip;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "parent_id", nullable = true)
    private Parent parent;

    @Column(name = "student_name", nullable = false)
    private String studentName;

    @Column(name = "assigned_pickup_time")
    private LocalTime assignedPickupTime;

    @Column(name = "home_location")
    private String homeLocation;
}
