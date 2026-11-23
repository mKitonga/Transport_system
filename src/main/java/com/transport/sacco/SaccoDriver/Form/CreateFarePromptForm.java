package com.transport.sacco.SaccoDriver.Form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateFarePromptForm {

    @NotBlank(message = "error.route.required")
    private String routeId;

    @NotBlank(message = "error.boarding.terminal.required")
    private String boardingTerminalId;

    @NotBlank(message = "error.dropoff.terminal.required")
    private String dropoffTerminalId;

    @NotBlank(message = "error.passenger.phone.required")
    private String passengerPhone;
}
