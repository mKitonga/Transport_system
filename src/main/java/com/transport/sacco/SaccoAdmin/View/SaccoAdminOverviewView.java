package com.transport.sacco.SaccoAdmin.View;

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
public class SaccoAdminOverviewView {
    private long pendingDriverApprovals;
    private long activeRoutes;
    private long queueWaiting;
    private long openComplaints;
}
