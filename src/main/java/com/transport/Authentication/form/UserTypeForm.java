package com.transport.Authentication.form;

import com.transport.Authentication.Entity.UsernameType;
import com.transport.liby.service.FormatUtil;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTypeForm extends UsernameForm {
    @NotNull(message = "error.invalid.username.type")
    private UsernameType usernameType;

    public String getAnonymisedUsername() {
        return switch (usernameType) {
            case EMAIL -> FormatUtil.maskEmail(getUsername());
            case PHONE_NUMBER -> getUsername();
        };
    }
}
