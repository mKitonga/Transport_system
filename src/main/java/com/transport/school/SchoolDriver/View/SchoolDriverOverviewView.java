package com.transport.school.SchoolDriver.View;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SchoolDriverOverviewView {
    private String driverName;
    private String schoolName;
    private String location;
    private long totalAssignments;
    private long sentAlerts;
}
