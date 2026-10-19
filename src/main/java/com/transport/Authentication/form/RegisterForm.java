package com.transport.Authentication.form;

import com.transport.Authentication.Validator.UniqueEmail;
import com.transport.Authentication.Validator.UniquePhoneNumber;
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
