package com.transport.trips.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterEducationalTripForm extends RegisterTripForm {

    @NotBlank
    @Size(max = 150)
    private String tripTitle;

    @NotBlank
    @Size(max = 255)
    private String destination;

    @NotNull
    @Positive
    private Integer durationDays;

    private LocalDate tripDate;

    @Size(max = 36)
    private String saccoAdminId;

    @Size(max = 36)
    private String saccoId;
}
