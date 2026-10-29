package com.transport.fares.entity;

import com.transport.liby.entity.BaseJpaEntity;
import com.transport.routes.Entity.Route;
import com.transport.routes.Entity.Terminal;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(
        name = "uk_fare_route_direction_day_period",
        columnNames = {
                "route_id",
                "boarding_terminal_id",
                "dropoff_terminal_id",
                "day_type",
                "time_period"
        }
))
@Getter
@Setter
public class Fare extends BaseJpaEntity {
    @Column(name = "time_period", nullable = false)
    @Enumerated(EnumType.STRING)
    private TimePeriod timePeriod;

    @Column(name = "fare_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal fareAmount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "boarding_terminal_id", nullable = false)
    private Terminal boardingTerminal;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "dropoff_terminal_id", nullable = false)
    private Terminal dropoffTerminal;

    @Column(name = "day_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayType dayType;
}
