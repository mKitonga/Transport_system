package com.transport.school.SchoolAdmin.View;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SchoolAdminOverviewView {

    private long pendingDriverApprovals;
    private long openComplaints;
    private long totalTrips;
    private long unreadNotifications;
}
