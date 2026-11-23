package com.transport.sacco.SaccoDriver.Form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SchoolTransportUpdateForm {

    @NotBlank(message = "error.subject.required")
    @Size(max = 255)
    private String subject;

    @NotBlank(message = "error.message.required")
    @Size(max = 1000)
    private String message;
}
