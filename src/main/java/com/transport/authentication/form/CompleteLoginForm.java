package com.transport.authentication.form;

import lombok.Getter;
import lombok.Setter;

import java.util.Locale;

@Getter
@Setter
public class CompleteLoginForm extends OtpForm{
    private Locale locale;
}
