package com.transport.Authentication.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
abstract class OtpForm {
    @NotBlank(message = "error.empty.otp")
    private String otp;
}
