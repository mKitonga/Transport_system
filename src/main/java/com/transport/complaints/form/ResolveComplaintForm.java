package com.transport.complaints.form;

import com.transport.liby.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResolveComplaintForm extends SessionUserIdForm {

    @NotBlank
    @Size(max = 2000)
    private String responseMessage;
}
