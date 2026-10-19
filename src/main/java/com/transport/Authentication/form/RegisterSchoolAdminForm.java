package com.transport.Authentication.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterSchoolAdminForm extends RegisterForm {
    private String schoolEntityId;

    private String location;
}
