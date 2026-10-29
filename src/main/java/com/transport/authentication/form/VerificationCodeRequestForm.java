package com.transport.authentication.form;

import com.transport.authentication.Entity.VerificationCodeUse;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationCodeRequestForm extends UserTypeForm {
    @NotNull(message = "error.invalid.otp.use")
    private VerificationCodeUse verificationCodeUse;
}

