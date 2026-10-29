package com.transport.trips.entity;

import com.transport.school.Entity.SchoolGrade;
import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.liby.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Getter
@Setter
public abstract class Trip extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_admin_id", nullable = false)
    private SchoolAdmin schoolAdmin;

    @Column(name = "school_name", nullable = false)
    private String schoolName;

    @Column(name = "location", nullable = false)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 24)
    private TripStatus status = TripStatus.DRAFT;

    @Enumerated(EnumType.STRING)
    @Column(name = "trip_type", nullable = false, length = 32)
    private TripType tripType;

    @Column(name = "grade")
    private SchoolGrade grade;

    @Column(name = "amount", precision = 10, scale = 2)
    private BigDecimal amount;
}
