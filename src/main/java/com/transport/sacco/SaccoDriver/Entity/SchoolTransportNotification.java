package com.transport.sacco.SaccoDriver.Entity;

import com.transport.liby.entity.BaseJpaEntity;
import com.transport.school.SchoolAdmin.Entity.SchoolAdmin;
import com.transport.trips.entity.Trip;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "school_transport_notifications")
@Getter
@Setter
public class SchoolTransportNotification extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "school_admin_id", nullable = false)
    private SchoolAdmin schoolAdmin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private SaccoDriver driver;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trip_id")
    private Trip trip;

    @Column(name = "school_name")
    private String schoolName;

    @Column(name = "location")
    private String location;

    @Column(name = "subject", nullable = false)
    private String subject;

    @Column(name = "message", nullable = false, length = 1000)
    private String message;

    @Column(name = "status", nullable = false, length = 32)
    private String status = "SENT";

    @Column(name = "sent_at", nullable = false)
    private LocalDateTime sentAt = LocalDateTime.now();
}
