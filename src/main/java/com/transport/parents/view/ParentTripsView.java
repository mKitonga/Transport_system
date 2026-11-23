package com.transport.parents.view;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParentTripsView {

    private ParentView child;
    private List<DailyTripItem> dailyTrips;
    private List<EducationalTripItem> educationalTrips;
    private List<ClosingTripItem> closingTrips;

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class DailyTripItem {
        private String tripId;
        private String routeName;
        private LocalTime pickupTime;
        private LocalTime dropoffTime;
        private BigDecimal amount;
        private String paymentStatus;
        private LocalTime assignedPickupTime;
        private String driverName;
        private String driverPhone;
    }

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class EducationalTripItem {
        private String tripId;
        private String tripTitle;
        private String destination;
        private int durationDays;
        private LocalDate tripDate;
        private BigDecimal amount;
        private String paymentStatus;
    }

    @Getter
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ClosingTripItem {
        private String tripId;
        private String routeName;
        private LocalDate tripDate;
        private BigDecimal amount;
        private String paymentStatus;
        private String driverName;
        private String driverPhone;
        private String numberPlate;
    }
}
