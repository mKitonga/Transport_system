package com.transport.authentication.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterSchoolAdminForm extends RegisterForm {
    private String schoolEntityId;

    private String location;
}
