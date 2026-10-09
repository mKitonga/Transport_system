package com.transport.Routes.Form;

import com.transport.liby.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class UpdateRouteTerminalsForm extends SessionUserIdForm {

    @NotNull
    private Set<@NotBlank @Size(max = 36) String> terminalIds;
}
