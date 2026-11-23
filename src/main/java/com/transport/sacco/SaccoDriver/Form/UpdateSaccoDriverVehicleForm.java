package com.transport.sacco.SaccoDriver.Form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateSaccoDriverVehicleForm {

    @NotBlank(message = "error.invalid.number.plate")
    private String numberPlate;

    private String matatuName;
}
