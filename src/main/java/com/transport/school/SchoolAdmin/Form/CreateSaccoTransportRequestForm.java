package com.transport.school.SchoolAdmin.Form;

import com.transport.liby.form.SessionUserIdForm;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateSaccoTransportRequestForm extends SessionUserIdForm {

    @NotBlank(message = "{error.sacco.admin.required}")
    private String saccoAdminId;

    @Min(value = 1, message = "{error.vehicles.min}")
    private int requestedVehicles;

    private String notes;
}
