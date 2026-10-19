package com.transport.Authentication.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterSaccoAdminForm extends RegisterForm {
    @NotBlank
    private String saccoEntityId;

    @NotBlank
    private String stationEntityId;
}
