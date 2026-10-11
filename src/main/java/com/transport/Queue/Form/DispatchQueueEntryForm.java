package com.transport.Queue.Form;

import com.transport.liby.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DispatchQueueEntryForm extends SessionUserIdForm {

    @NotBlank
    @Size(max = 36)
    private String routeId;
}
