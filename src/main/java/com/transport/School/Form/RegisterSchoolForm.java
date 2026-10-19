package com.transport.School.Form;

import com.transport.liby.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterSchoolForm extends SessionUserIdForm {
    @NotBlank
    private String name;

    @NotBlank
    private String location;
}
