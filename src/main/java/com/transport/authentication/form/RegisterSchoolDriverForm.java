package com.transport.authentication.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterSchoolDriverForm extends RegisterForm {
    @NotBlank
    private String schoolEntityId;

    @NotBlank
    private String location;
}
