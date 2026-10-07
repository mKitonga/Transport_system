package com.transport.Authentication.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PublicKeyForm extends OtpForm{
    @NotBlank(message = "error.invalid.public.key")
    private String publicKey;
}
