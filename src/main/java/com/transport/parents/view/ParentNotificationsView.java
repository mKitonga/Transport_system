package com.transport.parents.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.transport.school.View.SchoolTripPaymentView;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParentNotificationsView {

    private List<ParentTripsView.DailyTripItem> dailyAssignments;
    private List<ParentTripsView.ClosingTripItem> closingAssignments;
    private List<SchoolTripPaymentView> payments;
}
