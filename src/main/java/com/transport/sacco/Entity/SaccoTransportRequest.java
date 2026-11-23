package com.transport.sacco.Entity;

import com.transport.liby.entity.BaseJpaEntity;
import com.transport.sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "sacco_transport_requests")
@Getter
@Setter
public class SaccoTransportRequest extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_admin_id", nullable = false)
    private SchoolAdmin schoolAdmin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sacco_admin_id", nullable = false)
    private SaccoAdmin saccoAdmin;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "location")
    private String location;

    @Column(name = "requested_vehicles", nullable = false)
    private int requestedVehicles;

    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "status", nullable = false, length = 32)
    private String status = "PENDING";

    @Column(name = "requested_at", nullable = false)
    private LocalDateTime requestedAt = LocalDateTime.now();
}
