package com.transport.Authentication.form;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterSaccoDriverForm extends RegisterForm {
    private String saccoEntityId;
    private String assignedRouteEntityId;
    private String numberPlate;
    private String matatuName;
}
