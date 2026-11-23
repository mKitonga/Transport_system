package com.transport.sacco.SaccoDriver.View;

import com.transport.queue.View.QueueEntryView;
import com.transport.routes.View.RouteView;
import com.transport.user.entity.UserStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaccoDriverOverviewView {
    private String driverName;
    private String saccoName;
    private String numberPlate;
    private String matatuName;
    private UserStatus status;
    private RouteView assignedRoute;
    private QueueEntryView currentQueueEntry;
    private long totalFarePrompts;
    private long pendingDropoffs;
    private long schoolAssignmentsCount;
}
