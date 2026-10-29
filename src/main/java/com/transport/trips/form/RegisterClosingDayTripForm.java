package com.transport.trips.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterClosingDayTripForm extends RegisterTripForm {

    @NotBlank
    @Size(max = 36)
    private String routeId;

    @NotNull
    private LocalDate tripDate;

    @NotBlank
    @Size(max = 36)
    private String saccoAdminId;

    @Size(max = 36)
    private String saccoId;
}
