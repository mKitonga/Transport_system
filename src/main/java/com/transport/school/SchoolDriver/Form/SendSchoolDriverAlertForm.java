package com.transport.school.SchoolDriver.Form;

import com.transport.liby.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendSchoolDriverAlertForm extends SessionUserIdForm {

    @NotBlank(message = "{error.subject.required}")
    @Size(max = 255)
    private String subject;

    @NotBlank(message = "{error.message.required}")
    @Size(max = 1000, message = "{error.message.too.long}")
    private String message;
}
