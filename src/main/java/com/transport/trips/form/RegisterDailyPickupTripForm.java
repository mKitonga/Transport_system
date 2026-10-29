package com.transport.trips.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDailyPickupTripForm extends RegisterTripForm {

    @NotBlank(message = "error.empty.route")
    @Size(max = 36)
    private String routeId;

    @NotNull
    private LocalTime pickupTime;

    @NotNull
    private LocalTime dropoffTime;

}
