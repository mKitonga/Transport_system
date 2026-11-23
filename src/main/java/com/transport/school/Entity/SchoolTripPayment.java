package com.transport.school.Entity;

import com.transport.liby.entity.BaseJpaEntity;
import com.transport.parents.entity.Parent;
import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "school_trip_payments")
@Getter
@Setter
public class SchoolTripPayment extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent_id", nullable = false)
    private Parent parent;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_admin_id", nullable = false)
    private SchoolAdmin schoolAdmin;

    @Column(name = "trip_type", nullable = false, length = 32)
    private String tripType;

    @Column(name = "trip_id", nullable = false)
    private String tripId;

    @Column(name = "student_name")
    private String studentName;

    @Enumerated(EnumType.STRING)
    @Column(name = "grade")
    private SchoolGrade grade;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "mpesa_reference")
    private String mpesaReference;

    @Column(name = "checkout_request_id")
    private String checkoutRequestId;

    @Column(name = "status", nullable = false, length = 32)
    private String status = "SUBMITTED";

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "approved_by_entity_id")
    private String approvedByEntityId;
}
