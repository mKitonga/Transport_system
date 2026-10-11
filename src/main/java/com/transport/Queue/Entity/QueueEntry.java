package com.transport.Queue.Entity;

import com.transport.Routes.Entity.Route;
import com.transport.Routes.Entity.Terminal;
import com.transport.Sacco.SaccoAdmin.Entity.SaccoAdmin;
import com.transport.Sacco.SaccoDriver.Entity.SaccoDriver;
import com.transport.liby.entity.BaseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "driver_queue_entries",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_queue_driver_idempotency", columnNames = {"driver_id", "idempotency_key"}),
                @UniqueConstraint(name = "uk_queue_terminal_position", columnNames = {"terminal_id", "queue_position"})
        },
        indexes = {
                @Index(name = "idx_queue_admin_status_position", columnList = "sacco_admin_id,status,queue_position"),
                @Index(name = "idx_queue_driver_status", columnList = "driver_id,status")
        }
)
@Getter
@Setter
public class QueueEntry extends BaseJpaEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "sacco_admin_id", nullable = false)
    private SaccoAdmin saccoAdmin;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private SaccoDriver driver;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "terminal_id", nullable = false)
    private Terminal terminal;

    @Column(name = "idempotency_key", nullable = false, length = 128)
    private String idempotencyKey;

    @Column(name = "notified_at", nullable = false)
    private LocalDateTime notifiedAt;

    @Column(name = "queue_position", nullable = false)
    private int queuePosition;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_route_id")
    private Route assignedRoute;

    @Column(name = "assigned_route_name", length = 255)
    private String assignedRouteName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 24)
    private QueueEntryStatus status = QueueEntryStatus.WAITING;

    @Column(name = "departed_at")
    private LocalDateTime departedAt;
}
