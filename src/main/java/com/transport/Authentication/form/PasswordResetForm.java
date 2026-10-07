package com.transport.Authentication.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetForm extends OtpForm{
    @NotBlank(message = "error.invalid.password")
    private String password;
}
