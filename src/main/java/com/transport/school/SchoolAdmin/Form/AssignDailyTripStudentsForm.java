package com.transport.school.SchoolAdmin.Form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.transport.liby.form.SessionUserIdForm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class AssignDailyTripStudentsForm extends SessionUserIdForm {

    @NotBlank(message = "{error.school.driver.required}")
    private String schoolDriverId;

    @NotNull(message = "{error.pickup.time.required}")
    @JsonFormat(pattern = "HH:mm")
    private LocalTime assignedPickupTime;

    @NotEmpty(message = "{error.parent.ids.required}")
    private List<String> parentIds;
}
