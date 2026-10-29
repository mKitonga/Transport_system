package com.transport.sacco.Form;

import com.transport.sacco.Validator.UniqueSaccoName;
import com.transport.liby.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterSaccoForm extends SessionUserIdForm {
    @NotBlank
    @UniqueSaccoName
    private String name;
}
