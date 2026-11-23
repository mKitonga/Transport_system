package com.transport.sacco.SaccoDriver.Entity;

import com.transport.liby.entity.BaseJpaEntity;
import com.transport.routes.Entity.Route;
import com.transport.routes.Entity.Terminal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "sacco_passenger_payments")
@Getter
@Setter
public class PassengerPayment extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private SaccoDriver driver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "boarding_terminal_id", nullable = false)
    private Terminal boardingTerminal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dropoff_terminal_id", nullable = false)
    private Terminal dropoffTerminal;

    @Column(name = "passenger_phone", nullable = false, length = 32)
    private String passengerPhone;

    @Column(name = "fare_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal fareAmount;

    @Column(name = "prompt_status", nullable = false, length = 32)
    private String promptStatus = "STK_SENT";

    @Column(name = "cleared_at")
    private LocalDateTime clearedAt;

    @Column(name = "prompt_requested_at", nullable = false)
    private LocalDateTime promptRequestedAt = LocalDateTime.now();
}
