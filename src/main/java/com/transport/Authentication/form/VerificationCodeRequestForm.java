package com.transport.Authentication.form;

import com.transport.Authentication.Entity.VerificationCodeUse;
import jakarta.validation.constraints.NotNull;
import com.transport.Authentication.form.UserTypeForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerificationCodeRequestForm extends UserTypeForm {
    @NotNull(message = "error.invalid.otp.use")
    private VerificationCodeUse verificationCodeUse;
}

