package com.transport.school.SchoolAdmin.Form;

import com.transport.liby.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SendSaccoMessageForm extends SessionUserIdForm {

    @NotBlank(message = "error.sacco.admin.required")
    private String saccoAdminId;

    @NotBlank(message = "error.message.required")
    private String message;
}
