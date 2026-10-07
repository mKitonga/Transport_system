package com.transport.Authentication.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InitLoginForm extends UserTypeForm{
    @NotBlank(message = "error.invalid.password")
    private String password;
}
