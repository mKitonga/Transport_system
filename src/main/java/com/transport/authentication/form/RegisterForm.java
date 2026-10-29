package com.transport.authentication.form;

import com.transport.authentication.Validator.UniqueEmail;
import com.transport.authentication.Validator.UniquePhoneNumber;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterForm {

    @NotBlank
    private String name;

    @NotBlank
    @UniqueEmail
    private String email;

    @NotBlank
    @UniquePhoneNumber
    private String  phoneNumber;

    @NotBlank
    private String password;

}
