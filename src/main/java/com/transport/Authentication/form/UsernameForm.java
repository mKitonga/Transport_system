package com.transport.Authentication.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UsernameForm {
    @NotBlank(message = "error.invalid.username")
    private String username;

    private String sessionUserId;
}
