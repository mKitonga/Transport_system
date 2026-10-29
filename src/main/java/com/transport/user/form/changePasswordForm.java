package com.transport.user.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class changePasswordForm {
    @NotBlank(message = "error.invalid.password")
    private String password;
}
