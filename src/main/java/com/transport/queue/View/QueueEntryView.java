package com.transport.queue.View;

import com.transport.queue.Entity.QueueEntry;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Locale;

@Getter
public class QueueEntryView {

    private final String entityId;
    private final String driverId;
    private final String driverName;
    private final String driverPhoneNumber;
    private final String saccoAdminId;
    private final String terminalId;
    private final String terminalName;
    private final LocalDateTime notifiedAt;
    private final int queuePosition;
    private final String assignedRouteId;
    private final String assignedRouteName;
    private final String status;
    private final LocalDateTime departedAt;

    public QueueEntryView(QueueEntry entry) {
        this.entityId = entry.getEntityId();
        this.driverId = entry.getDriver().getEntityId();
        this.driverName = entry.getDriver().getName();
        this.driverPhoneNumber = entry.getDriver().getPhoneNumber();
        this.saccoAdminId = entry.getSaccoAdmin().getEntityId();
        this.terminalId = entry.getTerminal().getEntityId();
        this.terminalName = entry.getTerminal().getName();
        this.notifiedAt = entry.getNotifiedAt();
        this.queuePosition = entry.getQueuePosition();
        this.assignedRouteId = entry.getAssignedRoute() == null
                ? null
                : entry.getAssignedRoute().getEntityId();
        this.assignedRouteName = entry.getAssignedRouteName();
        this.status = entry.getStatus().name().toLowerCase(Locale.ROOT);
        this.departedAt = entry.getDepartedAt();
    }
}
