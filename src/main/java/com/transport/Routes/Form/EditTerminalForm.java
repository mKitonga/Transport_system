package com.transport.Routes.Form;

import com.transport.liby.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditTerminalForm extends SessionUserIdForm {

    @NotBlank
    @Size(max = 255)
    private String name;
}
