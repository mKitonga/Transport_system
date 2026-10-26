package com.transport.complaints.entity;

import com.transport.Routes.Entity.Route;
import com.transport.Sacco.Entity.Sacco;
import com.transport.School.Entity.School;
import com.transport.liby.entity.BaseJpaEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "complaints")
@Getter
@Setter
public class Complaints extends BaseJpaEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintService service;

    @Column(nullable = false, length = 2000)
    private String complaintMessage;

    @Column(length = 2000)
    private String responseMessage;

    @Column
    private String passengerPhoneNumber;

    @Column
    private String passengerEmail;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ComplaintStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sacco_id")
    private Sacco sacco;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id")
    private School school;

    @Column
    private String respondedByEntityId;

    @Column
    private LocalDateTime respondedAt;
}
